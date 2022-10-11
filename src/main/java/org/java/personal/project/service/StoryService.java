package org.java.personal.project.service;

import org.java.personal.project.dto.request.story.StoryCollectionRequestDTO;
import org.java.personal.project.dto.request.story.StoryCollectionWhenUpdateRequestDTO;
import org.java.personal.project.dto.request.story.StoryRequestDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

@Service
public interface StoryService {

    StatusResponse createStory(StoryRequestDTO storyRequestDTO, String userId) throws Exception;

    StatusResponse getUserStoryByUserId(String userId);

    StatusResponse createStoryCollection(StoryCollectionRequestDTO storyCollectionRequestDTO, String userId);

    StatusResponse updateStoryCollection(StoryCollectionWhenUpdateRequestDTO storyCollectionWhenUpdateRequestDTO, String userId);

    StatusResponse getCurrentStoryByStoryIdAndUserId(String postId, String userId) throws IOException;

    StatusResponse getAvailableStoryFromOtherWithin1DayByItsFollowing(String userId) throws IOException, ParseException;

    StatusResponse getDataFromRedis(String userId);
}
