package org.java.personal.project.controller;

import org.java.personal.project.dto.request.story.StoryCollectionRequestDTO;
import org.java.personal.project.dto.request.story.StoryCollectionWhenUpdateRequestDTO;
import org.java.personal.project.dto.request.story.StoryRequestDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1")
public class StoryController {

    private final StoryService storyService;

    @Autowired
    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @PostMapping("/createStory/{userId}")
    public ResponseEntity createStory(@ModelAttribute StoryRequestDTO storyRequestDTO, @PathVariable String userId) throws Exception {
        StatusResponse createStoryResponse = storyService.createStory(storyRequestDTO, userId);
        return new ResponseEntity(createStoryResponse, createStoryResponse.getResponse());
    }

    @GetMapping("/getStoryArchive/{userId}")
    public ResponseEntity getUserStoryByUserId(@PathVariable String userId){
        StatusResponse getUserStoryResponse = storyService.getUserStoryByUserId(userId);
        return new ResponseEntity(getUserStoryResponse, getUserStoryResponse.getResponse());
    }

    @PostMapping("/createCollection")
    public ResponseEntity createStoryCollection(@RequestBody StoryCollectionRequestDTO storyCollectionRequestDTO, @RequestHeader(value = "userId") String userId){
        StatusResponse createStoryCollectionResponse = storyService.createStoryCollection(storyCollectionRequestDTO, userId);
        return new ResponseEntity(createStoryCollectionResponse, createStoryCollectionResponse.getResponse());
    }

    @PostMapping("/updateCollection")
    public ResponseEntity updateStoryCollection(@RequestHeader(value = "userId") String userId, @RequestBody StoryCollectionWhenUpdateRequestDTO storyCollectionWhenUpdateRequestDTO){
        StatusResponse updateStoryResponse = storyService.updateStoryCollection(storyCollectionWhenUpdateRequestDTO, userId);
        return new ResponseEntity(updateStoryResponse, updateStoryResponse.getResponse());
    }

    @GetMapping("/getUserStory/{storyId}")
    public ResponseEntity getUserStoryByPostId(@PathVariable String storyId, @RequestHeader(value = "userId") String userId) throws IOException {
        StatusResponse getUserStoryResponse = storyService.getCurrentStoryByStoryIdAndUserId(storyId, userId);
        return new ResponseEntity(getUserStoryResponse, getUserStoryResponse.getResponse());
    }

    @GetMapping("/getAvailableStoryFromOther/{userId}")
    public ResponseEntity getAvailableStoryFromOtherByItsFollowingByUserId(@PathVariable String userId) throws IOException {
        StatusResponse getAvailableStoryResponse = storyService.getAvailableStoryFromOtherWithin1DayByItsFollowing(userId);
        return new ResponseEntity(getAvailableStoryResponse, getAvailableStoryResponse.getResponse());
    }

}
