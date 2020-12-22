package org.java.personal.project.controller;

import org.java.personal.project.dto.request.post.CommentPostDTO;
import org.java.personal.project.dto.request.post.LikePostDTO;
import org.java.personal.project.dto.request.post.UpdatePostDTO;
import org.java.personal.project.dto.request.post.UserPostDTO;
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
    private ResponseEntity postPictureFromUser(@ModelAttribute UserPostDTO userPostDTO, @PathVariable String userId) throws Exception {
        StatusResponse postPictureResponse = postService.postPictureFromUser(userPostDTO, userId);
        return new ResponseEntity(postPictureResponse, postPictureResponse.getResponse());
    }

    @GetMapping("/getAllUserPost/{userId}")
    private ResponseEntity getUserPostById(@PathVariable String userId) throws IOException {
        StatusResponse getUserPostResponse = postService.getUserPostById(userId);
        return new ResponseEntity(getUserPostResponse, getUserPostResponse.getResponse());
    }

    @PostMapping("/comment/{postId}")
    private ResponseEntity commentToOtherUser(@PathVariable String postId, @RequestBody CommentPostDTO commentPostDTO){
        StatusResponse commentResponse = postService.commentToUserPost(commentPostDTO, postId);
        return new ResponseEntity(commentResponse, commentResponse.getResponse());
    }

    @PostMapping("/likePost")
    private ResponseEntity likePost(@RequestBody LikePostDTO likePostDTO){
        StatusResponse likeResponse = postService.likePost(likePostDTO.getPostId(), likePostDTO.getUserId());
        return new ResponseEntity(likeResponse, likeResponse.getResponse());
    }

    @GetMapping("/getPost/{postId}/{userId}")
    private ResponseEntity getOnePostFromUser(@PathVariable String postId, @PathVariable String userId) throws IOException {
        StatusResponse getPostResponse = postService.getOnePostByUserId(postId,userId);
        return new ResponseEntity(getPostResponse, getPostResponse.getResponse());
    }

    @PutMapping("/updatePost/{userId}")
    private ResponseEntity updateCaption(@RequestBody UpdatePostDTO updatePostDTO, @PathVariable String userId){
        StatusResponse updatePostResponse = postService.updateCaptionPost(updatePostDTO, userId);
        return new ResponseEntity(updatePostResponse, updatePostResponse.getResponse());
    }

    @DeleteMapping("/deletePost/{postId}/{userId}")
    private ResponseEntity deletePost(@PathVariable String postId, @PathVariable String userId){
        StatusResponse deletePostResponse = postService.deleteUserPostByPostId(postId, userId);
        return new ResponseEntity(deletePostResponse, deletePostResponse.getResponse());
    }

}
