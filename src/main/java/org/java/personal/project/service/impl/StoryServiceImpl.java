package org.java.personal.project.service.impl;

import org.java.personal.project.dao.StoryRepository;
import org.java.personal.project.dao.UserRepository;
import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.Story;
import org.java.personal.project.dto.request.story.StoryRequestDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.service.StoryService;
import org.java.personal.project.util.ConvertImageOrVideoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.java.personal.project.constant.AppEnum.*;

@Service
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final ConvertImageOrVideoUtil convertImageOrVideoUtil;

    @Autowired
    public StoryServiceImpl(StoryRepository storyRepository, UserRepository userRepository, ConvertImageOrVideoUtil convertImageOrVideoUtil) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
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
        currentStory.setMentionPeople(insertMentionPeopleInTheStory(storyRequestDTO.getMentionUsers()));
        convertImageOrVideoUtil.convertImage(storyRequestDTO.getStoryPost().getBytes(), storyRequestDTO.getStoryPost(), storyPosts);

        storyRepository.save(currentStory);
        return statusResponse.statusCreated(STORY_HAS_BEEN_CREATED.getMessage(), currentStory);
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
