package org.java.personal.project.service.impl;

import org.java.personal.project.dao.StoryCollectionRepository;
import org.java.personal.project.dao.StoryRepository;
import org.java.personal.project.dao.UserRepository;
import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.Story;
import org.java.personal.project.domain.StoryCollection;
import org.java.personal.project.dto.request.story.StoryCollectionRequestDTO;
import org.java.personal.project.dto.request.story.StoryRequestDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.dto.response.story.HeadStoryResponse;
import org.java.personal.project.dto.response.story.StoryResponse;
import org.java.personal.project.service.StoryService;
import org.java.personal.project.util.ConvertImageOrVideoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.java.personal.project.constant.AppEnum.*;

@Service
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final StoryCollectionRepository storyCollectionRepository;
    private final ConvertImageOrVideoUtil convertImageOrVideoUtil;

    @Autowired
    public StoryServiceImpl(StoryRepository storyRepository, UserRepository userRepository, StoryCollectionRepository storyCollectionRepository, ConvertImageOrVideoUtil convertImageOrVideoUtil) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
        this.storyCollectionRepository = storyCollectionRepository;
        this.convertImageOrVideoUtil = convertImageOrVideoUtil;
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
        currentStory.setMentionPeople((storyRequestDTO.getMentionUsers() == null || storyRequestDTO.getMentionUsers().size() == 0) ? new ArrayList<>() : insertMentionPeopleInTheStory(storyRequestDTO.getMentionUsers()));
        convertImageOrVideoUtil.convertImage(storyRequestDTO.getStoryPost().getBytes(), storyRequestDTO.getStoryPost(), storyPosts);

        storyRepository.save(currentStory);
        return statusResponse.statusCreated(STORY_HAS_BEEN_CREATED.getMessage(), currentStory);
    }

    @Override
    public StatusResponse getUserStoryByUserId(String userId) {
        StatusResponse statusResponse = new StatusResponse();
        HeadStoryResponse headStoryResponse = new HeadStoryResponse();
        List<StoryResponse> storyResponses = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(YOUR_USERNAME_WITH_ID.getMessage(), userId + IS_NOT_EXISTS.getMessage());

        List<Story> currentUsersStories = storyRepository.findStoryByCurrentUserStory(currentUser);
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
            return statusResponse.statusNotFound(YOUR_USERNAME_WITH_ID.getMessage() + userId + IS_NOT_EXISTS, null);

        StoryCollection storyCollection = new StoryCollection(
                storyCollectionRequestDTO.getCollectionName(),
                currentUser,
                insertStoriesIntoCollection(storyCollectionRequestDTO)
        );

        storyCollectionRepository.save(storyCollection);
        return statusResponse.statusCreated(SUCCESSFULLY_CREATE_COLLECTION.getMessage(), storyCollection);
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
