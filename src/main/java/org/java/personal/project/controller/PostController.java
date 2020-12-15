package org.java.personal.project.controller;

import org.java.personal.project.dto.request.UserPostDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/createPost/{userId}")
    private ResponseEntity postPictureFromUser(@ModelAttribute UserPostDTO userPostDTO, @PathVariable String userId) throws IOException {
        StatusResponse postPictureResponse = postService.postPictureFromUser(userPostDTO, userId);
        return new ResponseEntity(postPictureResponse, postPictureResponse.getResponse());
    }

    @GetMapping("/getUserPost/{userId}")
    private ResponseEntity getUserPostById(@PathVariable String userId){
        StatusResponse getUserPostResponse = postService.getUserPostById(userId);
        return new ResponseEntity(getUserPostResponse, getUserPostResponse.getResponse());
    }

}
