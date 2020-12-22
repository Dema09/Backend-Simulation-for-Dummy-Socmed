package org.java.personal.project.service;

import org.java.personal.project.dto.request.story.StoryRequestDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface StoryService {

    StatusResponse createStory(StoryRequestDTO storyRequestDTO, String userId) throws Exception;
}
