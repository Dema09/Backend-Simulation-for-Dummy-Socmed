package org.java.personal.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.java.personal.project.repository.*;
import org.java.personal.project.domain.*;
import org.java.personal.project.dto.request.story.StoryCollectionRequestDTO;
import org.java.personal.project.dto.request.story.StoryCollectionWhenUpdateRequestDTO;
import org.java.personal.project.dto.request.story.StoryRequestDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.dto.response.story.*;
import org.java.personal.project.service.StoryService;
import org.java.personal.project.util.DateUtil;
import org.java.personal.project.util.ImageOrVideoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
    private final DateUtil dateUtil;

    @Autowired
    public StoryServiceImpl(StoryRepository storyRepository,
                            UserRepository userRepository,
                            StoryCollectionRepository storyCollectionRepository,
                            PostOrStoryLocationRepository postOrStoryLocationRepository,
                            ImageOrVideoUtil imageOrVideoUtil,
                            StoryLatestRepository storyLatestRepository,
                            FollowerAndFollowingRepository followerAndFollowingRepository,
                            DateUtil dateUtil) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
        this.storyCollectionRepository = storyCollectionRepository;
        this.postOrStoryLocationRepository = postOrStoryLocationRepository;
        this.imageOrVideoUtil = imageOrVideoUtil;
        this.storyLatestRepository = storyLatestRepository;
        this.followerAndFollowingRepository= followerAndFollowingRepository;
        this.dateUtil = dateUtil;
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
        currentStory.setMentionPeople(storyRequestDTO.getMentionUsers() == null || storyRequestDTO.getMentionUsers().isEmpty() ? new ArrayList<>() : insertMentionPeopleInTheStory(storyRequestDTO.getMentionUsers()));
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
                        .createdDate(currentStory.getCreatedAt())
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
            storyResponse.setMentionedUsers(insertMentionedUsers(story));
            storyResponse.setStoryFileBase64(story.getStoryFileName());

            storyResponses.add(storyResponse);
        }
        headStoryResponse.setStories(storyResponses);
        headStoryResponse.setUsername(currentUser.getUsername());
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

        oneHeadStoryResponse.setStory(storyResponse);
        oneHeadStoryResponse.setUsername(currentUser.getUsername());

        return statusResponse.statusOk(oneHeadStoryResponse);
       }

    @Override
    @Cacheable(value = "storyLatests", key = "#userId")
    public StatusResponse getAvailableStoryFromOtherWithin1DayByItsFollowing(String userId) throws IOException, ParseException {
        StatusResponse statusResponse = new StatusResponse();
        UserStoriesResponse userStoriesResponse = new UserStoriesResponse();

        DummyUser dummyUser = userRepository.findOne(userId);

        if(dummyUser == null)
            return statusResponse.statusNotFound("This User with Id " + userId + " doesn't exists!", null);

        mapStoryBasedOnCurrentUserFollowing(dummyUser, userStoriesResponse);
        return statusResponse.statusOk(userStoriesResponse);
    }

    @Override
    public StatusResponse deleteStoryByUserIdAndStoryId(String storyId, String userId) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser dummyUser = userRepository.findOne(userId);
        if(dummyUser == null)
            return statusResponse.statusNotFound("This User with Id " + userId + " doesn't exists!", null);

        Story story = storyRepository.findStoryByStoryIdAndCurrentUserStory(storyId, dummyUser);
        if(story == null)
            return statusResponse.statusNotFound(STORY_NOT_FOUND.getMessage(), null);

        StoryLatest storyLatest = storyLatestRepository.findOne(story.getStoryId());
        if(storyLatest != null)
            storyLatestRepository.delete(storyLatest);

        storyRepository.delete(story);

        return statusResponse.statusOk(SUCCESSFULLY_DELETE_STORY.getMessage());
    }

    private void mapStoryBasedOnCurrentUserFollowing(DummyUser dummyUser, UserStoriesResponse userStoriesResponse) throws IOException, ParseException {
        List<HeadStoryResponse> headStoryResponses = new ArrayList<>();

        FollowerAndFollowing followerAndFollowing = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(dummyUser);
        for(DummyUser currentFollowingUser : followerAndFollowing.getFollowings()){
            List<StoryLatest> storyLatests = storyLatestRepository.findAllByUserId(currentFollowingUser.getId());
            if(!storyLatests.isEmpty())
                headStoryResponses.add(mapHeadStoryResponse(storyLatests, currentFollowingUser));
        }
        userStoriesResponse.setStoryResponses(headStoryResponses);
    }

    private HeadStoryResponse mapHeadStoryResponse(List<StoryLatest> storyLatests, DummyUser currentFollowingUser) throws IOException, ParseException {
        HeadStoryResponse headStoryResponse = new HeadStoryResponse();

        headStoryResponse.setStories(insertToStoryResponse(storyLatests));
        headStoryResponse.setUsername(currentFollowingUser.getUsername());

        return headStoryResponse;
    }

    private List<StoryResponse> insertToStoryResponse(List<StoryLatest> storyLatests) throws IOException, ParseException {
        List<StoryResponse> storyResponses = new ArrayList<>();

        for(StoryLatest storyLatest : storyLatests){
            StoryResponse storyResponse = new StoryResponse();

            storyResponse.setStoryId(storyLatest.getStoryId());
            storyResponse.setStoryFileBase64(imageOrVideoUtil.convertOneFile(storyLatest.getStoryFileName()));
            storyResponse.setTimeDiffToString(dateUtil.countTimeDifferentThenReturnString(storyLatest.getCreatedDate(), new Date()));
            storyResponse.setMentionedUsers(storyLatest.getMentionedUsername() != null ? loadToMentionedUserResponse(storyLatest) : null);

            storyResponses.add(storyResponse);
        }

        return storyResponses;
    }

    private List<String> loadToMentionedUserResponse(StoryLatest storyLatest) {
        List<String> mentionedUsernames = new ArrayList<>();
        for(DummyUser dummyUser : storyLatest.getMentionedUsername()){
            mentionedUsernames.add(dummyUser.getUsername());
        }
        return mentionedUsernames;
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
