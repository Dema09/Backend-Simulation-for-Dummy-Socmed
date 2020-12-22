package org.java.personal.project.controller;

import org.java.personal.project.dto.request.story.StoryRequestDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class StoryController {

    private final StoryService storyService;

    @Autowired
    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @PostMapping("/createStory/{userId}")
    private ResponseEntity createStory(@ModelAttribute StoryRequestDTO storyRequestDTO, @PathVariable String userId) throws Exception {
        StatusResponse createStoryResponse = storyService.createStory(storyRequestDTO, userId);
        return new ResponseEntity(createStoryResponse, createStoryResponse.getResponse());
    }
}
