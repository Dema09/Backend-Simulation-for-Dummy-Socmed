package org.java.personal.project.service;

import org.java.personal.project.dto.request.story.StoryRequestDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.springframework.stereotype.Service;

@Service
public interface StoryService {

    StatusResponse createStory(StoryRequestDTO storyRequestDTO, String userId) throws Exception;

    StatusResponse getUserStoryByUserId(String userId);
}
