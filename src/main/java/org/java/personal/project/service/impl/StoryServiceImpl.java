package org.java.personal.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.java.personal.project.dao.*;
import org.java.personal.project.domain.*;
import org.java.personal.project.dto.request.story.StoryCollectionRequestDTO;
import org.java.personal.project.dto.request.story.StoryCollectionWhenUpdateRequestDTO;
import org.java.personal.project.dto.request.story.StoryRequestDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.dto.response.story.HeadStoryResponse;
import org.java.personal.project.dto.response.story.OneHeadStoryResponse;
import org.java.personal.project.dto.response.story.StoryResponse;
import org.java.personal.project.dto.response.story.StoryResponseAfterUpdate;
import org.java.personal.project.service.StoryService;
import org.java.personal.project.util.ImageOrVideoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.java.personal.project.enumeration.AppEnum.*;

@Service
@Slf4j
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final StoryCollectionRepository storyCollectionRepository;
    private final PostOrStoryLocationRepository postOrStoryLocationRepository;
    private final ImageOrVideoUtil imageOrVideoUtil;
    private final StoryLatestRepository storyLatestRepository;
    private final FollowerAndFollowingRepository followerAndFollowingRepository;

    @Autowired
    public StoryServiceImpl(StoryRepository storyRepository,
                            UserRepository userRepository,
                            StoryCollectionRepository storyCollectionRepository,
                            PostOrStoryLocationRepository postOrStoryLocationRepository,
                            ImageOrVideoUtil imageOrVideoUtil,
                            StoryLatestRepository storyLatestRepository,
                            FollowerAndFollowingRepository followerAndFollowingRepository) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
        this.storyCollectionRepository = storyCollectionRepository;
        this.postOrStoryLocationRepository = postOrStoryLocationRepository;
        this.imageOrVideoUtil = imageOrVideoUtil;
        this.storyLatestRepository = storyLatestRepository;
        this.followerAndFollowingRepository= followerAndFollowingRepository;
    }

    @Override
    public StatusResponse createStory(StoryRequestDTO storyRequestDTO, String userId) throws Exception {
        StatusResponse statusResponse = new StatusResponse();
        DummyUser currentUser = userRepository.findOne(userId);
        List<String> storyPosts = new ArrayList<>();
        storyPosts.add(storyRequestDTO.getStoryPost().getOriginalFilename());

        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage(),null);

        Story currentStory = new Story();
        currentStory.setStoryFileName(storyRequestDTO.getStoryPost().getOriginalFilename());
        currentStory.setCurrentUserStory(currentUser);
        currentStory.setMentionPeople(storyRequestDTO.getMentionUsers().isEmpty() ? new ArrayList<>() : insertMentionPeopleInTheStory(storyRequestDTO.getMentionUsers()));
        currentStory.setStoryLocation(storyRequestDTO.getStoryLocation() != null ? insertStoryLocationDetails(storyRequestDTO) : null);

        imageOrVideoUtil.convertImage(storyRequestDTO.getStoryPost().getBytes(), storyRequestDTO.getStoryPost(), storyPosts);

        storyRepository.save(currentStory);
        saveStoryToRedis(currentStory);
        return statusResponse.statusCreated(STORY_HAS_BEEN_CREATED.getMessage(), currentStory);
    }

    private void saveStoryToRedis(Story currentStory) {
        StoryLatest storyLatest = StoryLatest
                        .builder()
                        .userId(currentStory.getCurrentUserStory().getId())
                        .storyId(currentStory.getStoryId())
                        .username(currentStory.getCurrentUserStory().getUsername())
                        .storyLocation(currentStory.getStoryLocation() != null ? currentStory.getStoryLocation().getLocationName() : "")
                        .storyFileName(currentStory.getStoryFileName())
                        .isCloseFriendMode(currentStory.isCloseFriendMode())
                        .mentionedUsername(currentStory.getMentionPeople())
                        .build();

        storyLatestRepository.save(storyLatest);
        log.info("Story Latest on Redis: {}", storyLatestRepository.findOne(storyLatest.getStoryId()));
    }

    private PostOrStoryLocation insertStoryLocationDetails(StoryRequestDTO storyRequestDTO) {
        PostOrStoryLocation storyLocation = new PostOrStoryLocation();
        storyLocation.setLocationName(storyRequestDTO.getStoryLocation().getLocationName());
        storyLocation.setLocation(insertStoryLocation(storyRequestDTO));

        postOrStoryLocationRepository.save(storyLocation);
        return storyLocation;
    }

    private Location insertStoryLocation(StoryRequestDTO storyRequestDTO) {
        Location location = new Location(POINT.getMessage(),
                storyRequestDTO.getStoryLocation().getLongitude(),
                storyRequestDTO.getStoryLocation().getLatitude());

        return location;
    }

    @Override
    public StatusResponse getUserStoryByUserId(String userId) {
        StatusResponse statusResponse = new StatusResponse();
        HeadStoryResponse headStoryResponse = new HeadStoryResponse();
        List<StoryResponse> storyResponses = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(THIS_USER_WITH_ID.getMessage(), userId + IS_NOT_EXISTS.getMessage());

        List<Story> currentUsersStories = storyRepository.findStoriesByCurrentUserStory(currentUser);
        if(currentUsersStories.size() == 0 || currentUsersStories == null)
            return statusResponse.statusOk(new ArrayList<>());

        for(Story story : currentUsersStories){
            StoryResponse storyResponse = new StoryResponse();
            storyResponse.setStoryId(story.getStoryId());
            storyResponse.setUsername(story.getCurrentUserStory().getUsername());
            storyResponse.setMentionedUsers(insertMentionedUsers(story));
            storyResponse.setStoryFileBase64(story.getStoryFileName());

            storyResponses.add(storyResponse);
        }
        headStoryResponse.setStories(storyResponses);
        return statusResponse.statusOk(headStoryResponse);
    }

    @Override
    public StatusResponse createStoryCollection(StoryCollectionRequestDTO storyCollectionRequestDTO, String userId) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(THIS_USER_WITH_ID.getMessage() + userId + IS_NOT_EXISTS, null);

        StoryCollection storyCollection = new StoryCollection(
                storyCollectionRequestDTO.getCollectionName(),
                currentUser,
                insertStoriesIntoCollection(storyCollectionRequestDTO)
        );

        storyCollectionRepository.save(storyCollection);
        return statusResponse.statusCreated(SUCCESSFULLY_CREATE_COLLECTION.getMessage(), storyCollection);
    }

    @Override
    public StatusResponse updateStoryCollection(StoryCollectionWhenUpdateRequestDTO storyCollectionWhenUpdateRequestDTO, String userId) {
        StatusResponse statusResponse = new StatusResponse();
        DummyUser currentUser = userRepository.findOne(userId);
        StoryResponseAfterUpdate response = new StoryResponseAfterUpdate();
        List<String> failedCollectionIds = new ArrayList<>();

        if(currentUser == null)
            return statusResponse.statusNotFound(THIS_USER_WITH_ID.getMessage() + userId + IS_NOT_EXISTS.getMessage(), null);

        Iterator<String> deletedStoryIdIterator = storyCollectionWhenUpdateRequestDTO.getDeletedStoryIdList().iterator();
        while(deletedStoryIdIterator.hasNext()){
            String deletedStoryId = deletedStoryIdIterator.next();
            Story checkStory = storyRepository.findOne(deletedStoryId);
            if(!deletedStoryId.equals(checkStory.getStoryId()))
                failedCollectionIds.add(deletedStoryId);
        }

        StoryCollection storyCollection = storyCollectionRepository.findOne(storyCollectionWhenUpdateRequestDTO.getCollectionId());
        List<Story> stories = storyCollection.getStories();

        if(storyCollection == null)
            return statusResponse.statusNotFound(USER_STORY_IS_NOT_FOUND.getMessage() + storyCollectionWhenUpdateRequestDTO.getCollectionId(), null);

        //TODO: This functionality might allows users only to delete their stories,
        // delete and insert another stories, or only to insert stories to user's collection
        removeStoryFromCollection(storyCollection, storyCollectionWhenUpdateRequestDTO, stories);
        insertAnotherStoryFromCollection(storyCollection, storyCollectionWhenUpdateRequestDTO, stories);

        response.setCollectionId(storyCollection.getCollectionId());
        response.setFailedCollectionId(failedCollectionIds);
        response.setMessage(SUCCESSFULLY_UPDATE_COLLECTION_WITH_COLLECTION_ID.getMessage() + storyCollection.getCollectionId());

        return statusResponse.statusOk(response);
        
    }

    @Override
    public StatusResponse getCurrentStoryByStoryIdAndUserId(String storyId, String userId) throws IOException {
        StatusResponse statusResponse = new StatusResponse();
        OneHeadStoryResponse oneHeadStoryResponse = new OneHeadStoryResponse();
        StoryResponse storyResponse = new StoryResponse();

        List<String> storyNames = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(THIS_USER_WITH_ID.getMessage() + userId + IS_NOT_EXISTS.getMessage(), null);

        Story currentStory = storyRepository.findStoryByStoryIdAndCurrentUserStory(storyId, currentUser);
        if(currentStory == null)
            return statusResponse.statusNotFound(USER_STORY_IS_NOT_FOUND.getMessage() + storyId, null);

        storyNames.add(currentStory.getStoryFileName());
        storyResponse.setStoryId(currentStory.getStoryId());
        storyResponse.setStoryFileBase64(imageOrVideoUtil.convertOneFile(currentStory.getStoryFileName()));
        storyResponse.setMentionedUsers(insertMentionedUsers(currentStory));
        storyResponse.setUsername(currentUser.getUsername());

        oneHeadStoryResponse.setStory(storyResponse);
        return statusResponse.statusOk(oneHeadStoryResponse);
       }

    @Override
    public StatusResponse getAvailableStoryFromOtherWithin1DayByItsFollowing(String userId) throws IOException {
        StatusResponse statusResponse = new StatusResponse();
        HeadStoryResponse headStoryResponse = new HeadStoryResponse();

        DummyUser dummyUser = userRepository.findOne(userId);

        if(dummyUser == null)
            return statusResponse.statusNotFound("This User with Id " + userId + " doesn't exists!", null);

        headStoryResponse.setStories(mapStoryBasedOnCurrentUserFollowing(dummyUser));
        return statusResponse.statusOk(headStoryResponse);
    }

    private List<StoryResponse> mapStoryBasedOnCurrentUserFollowing(DummyUser dummyUser) throws IOException {
        List<StoryResponse> storyResponses = new ArrayList<>();

        FollowerAndFollowing followerAndFollowing = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(dummyUser);
        for(DummyUser currentFollowingUser : followerAndFollowing.getFollowings()){
            StoryLatest storyLatest = storyLatestRepository.findStoryLatestByUserId(currentFollowingUser.getId());
            if(storyLatest != null)
                storyResponses.add(insertToStoryResponse(storyLatest, new StoryResponse()));
        }
        return storyResponses;
    }

    private StoryResponse insertToStoryResponse(StoryLatest storyLatest, StoryResponse storyResponse) throws IOException {
        storyResponse.setStoryId(storyLatest.getStoryId());
        storyResponse.setStoryFileBase64(imageOrVideoUtil.convertOneFile(storyLatest.getStoryFileName()));
        storyResponse.setUsername(storyLatest.getUsername());
//        storyResponse.setTimeDiffToString();

        return storyResponse;
    }

    private void insertAnotherStoryFromCollection(StoryCollection storyCollection, StoryCollectionWhenUpdateRequestDTO storyCollectionWhenUpdateRequestDTO, List<Story> stories) {
        if(storyCollectionWhenUpdateRequestDTO.getAddedStoryIdList() == null)
            return;

        for(String storyId : storyCollectionWhenUpdateRequestDTO.getAddedStoryIdList()){
            Story story = storyRepository.findOne(storyId);
            stories.add(story);
        }
        storyCollection.setStories(stories);
        storyCollectionRepository.save(storyCollection);
    }

    private void removeStoryFromCollection(StoryCollection storyCollection, StoryCollectionWhenUpdateRequestDTO storyCollectionWhenUpdateRequestDTO, List<Story> stories) {
        if(storyCollectionWhenUpdateRequestDTO.getDeletedStoryIdList() == null)
            return;

        Iterator<Story> storyIterator = storyCollection.getStories().iterator();
        for(String deletedStoryId : storyCollectionWhenUpdateRequestDTO.getDeletedStoryIdList()){
            while(storyIterator.hasNext()){
                Story story = storyIterator.next();
                if(story.getStoryId().equals(deletedStoryId))
                    storyIterator.remove();
            }
        }

        storyCollection.setStories(stories);
        storyCollectionRepository.save(storyCollection);
    }

    private List<Story> insertStoriesIntoCollection(StoryCollectionRequestDTO storyCollectionRequestDTO) {
        List<Story> stories = new ArrayList<>();

        for(String storyId : storyCollectionRequestDTO.getStories()){
            Story currentStory = storyRepository.findOne(storyId);
            stories.add(currentStory);
        }
        return stories;
    }

    private List<String> insertMentionedUsers(Story story) {
        List<String> mentionUsers = new ArrayList<>();
        for(DummyUser dummyUser: story.getMentionPeople()){
            mentionUsers.add(dummyUser.getUsername());
        }
        return mentionUsers;
    }

    private List<DummyUser> insertMentionPeopleInTheStory(List<String> mentionUsernames) {
        List<DummyUser> mentionUsers = new ArrayList<>();
        for(String mentionUsername : mentionUsernames){
            DummyUser mentionUser = userRepository.findDummyUserByUsername(mentionUsername);
            mentionUsers.add(mentionUser);
        }
        return mentionUsers;
    }

}
