package org.java.personal.project.controller;

import org.java.personal.project.dto.request.post.*;
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
    public ResponseEntity postPictureFromUser(@ModelAttribute UserPostDTO userPostDTO, @PathVariable String userId) throws Exception {
        StatusResponse postPictureResponse = postService.postPictureFromUser(userPostDTO, userId);
        return new ResponseEntity(postPictureResponse, postPictureResponse.getResponse());
    }

    @GetMapping("/getAllUserPost/{userId}")
    public ResponseEntity getUserPostById(@PathVariable String userId) throws IOException {
        StatusResponse getUserPostResponse = postService.getUserPostById(userId);
        return new ResponseEntity(getUserPostResponse, getUserPostResponse.getResponse());
    }

    @PostMapping("/comment/{postId}")
    public ResponseEntity commentToOtherUser(@PathVariable String postId, @RequestBody CommentPostDTO commentPostDTO){
        StatusResponse commentResponse = postService.commentToUserPost(commentPostDTO, postId);
        return new ResponseEntity(commentResponse, commentResponse.getResponse());
    }

    @PostMapping("/likePost")
    public ResponseEntity likePost(@RequestBody LikeOrUnlikePostDTO likeOrUnlikePostDTO){
        StatusResponse likeResponse = postService.likePost(likeOrUnlikePostDTO.getPostId(), likeOrUnlikePostDTO.getUserId());
        return new ResponseEntity(likeResponse, likeResponse.getResponse());
    }

    @PostMapping("/unlikePost")
    public ResponseEntity unlikePost(@RequestBody LikeOrUnlikePostDTO likeOrUnlikePostDTO){
        StatusResponse unlikePostResponse = postService.unlikePost(likeOrUnlikePostDTO.getPostId(), likeOrUnlikePostDTO.getUserId());
        return new ResponseEntity(unlikePostResponse, unlikePostResponse.getResponse());
    }

    @GetMapping("/getPost/{postId}")
    public ResponseEntity getOnePostFromUser(@PathVariable String postId, @RequestHeader(value = "userId") String userId) throws IOException {
        StatusResponse getPostResponse = postService.getOnePostByUserId(postId,userId);
        return new ResponseEntity(getPostResponse, getPostResponse.getResponse());
    }

    @PutMapping("/updatePost")
    public ResponseEntity updateCaption(@RequestBody UpdatePostDTO updatePostDTO, @RequestHeader(value = "userId") String userId){
        StatusResponse updatePostResponse = postService.updateCaptionPost(updatePostDTO, userId);
        return new ResponseEntity(updatePostResponse, updatePostResponse.getResponse());
    }

    @DeleteMapping("/deletePost/{postId}/{userId}")
    public ResponseEntity deletePost(@PathVariable String postId, @PathVariable String userId){
        StatusResponse deletePostResponse = postService.deleteUserPostByPostId(postId, userId);
        return new ResponseEntity(deletePostResponse, deletePostResponse.getResponse());
    }

    @PostMapping("/savePost")
    public ResponseEntity savePost(@RequestHeader (value = "userId") String userId, @RequestBody SavedPostToCollectionDTO savedPostToCollectionDTO){
        StatusResponse savePostsResponse = postService.savePostsToCollection(savedPostToCollectionDTO, userId);
        return new ResponseEntity(savePostsResponse, savePostsResponse.getResponse());
    }

    @GetMapping("/getUserPostCollection")
    public ResponseEntity getUserPostCollection(@RequestHeader (value = "userId") String userId) throws IOException {
        StatusResponse getUserPostCollectionResponse = postService.getUserPostCollectionByUserId(userId);
        return new ResponseEntity(getUserPostCollectionResponse, getUserPostCollectionResponse.getResponse());
    }

    @GetMapping("/getOneUserPostCollection/{postCollectionId}")
    public ResponseEntity getOneUserPostCollection(@PathVariable String postCollectionId, @RequestHeader (value = "userId") String userId) throws IOException {
        StatusResponse getOneUserPostCollectionResponse = postService.getOnePostCollectionByPostCollectionId(postCollectionId, userId);
        return new ResponseEntity(getOneUserPostCollectionResponse, getOneUserPostCollectionResponse.getResponse());

    }

    @PutMapping("/updatePostCollection/{postCollectionId}")
    public ResponseEntity updatePostCollection(@RequestHeader (value = "userId") String userId, @PathVariable String postCollectionId, @RequestBody UpdatePostCollectionDTO updatePostCollectionDTO){
        StatusResponse updatePostCollectionResponse = postService.updatePostCollectionByPostCollectionId(userId, postCollectionId, updatePostCollectionDTO);
        return new ResponseEntity(updatePostCollectionResponse, updatePostCollectionResponse.getResponse());
    }

    @PutMapping("/updatePostCollectionContent/{postCollectionId}")
    public ResponseEntity updatePostCollectionContent(@PathVariable String postCollectionId, @RequestHeader (value = "userId") String userId, @RequestBody UpdatePostCollectionContentDTO updatePostCollectionContentDTO){
        StatusResponse updatePostCollectionContentResponse = postService.updatePostCollectionContentByPostCollectionId(userId, postCollectionId, updatePostCollectionContentDTO);
        return new ResponseEntity(updatePostCollectionContentResponse, updatePostCollectionContentResponse.getResponse());
    }

    @GetMapping("/getTaggedPost")
    public ResponseEntity getTaggedPostByUserId(@RequestHeader (value = "userId") String userId) throws IOException {
        StatusResponse getTaggedPostResponse = postService.getTaggedPostByUserId(userId);
        return new ResponseEntity(getTaggedPostResponse, getTaggedPostResponse.getResponse());
    }

    @PutMapping("/updateComment/{postId}")
    public ResponseEntity updateComment(@PathVariable String postId, @RequestBody CommentPostDTO commentPostDTO){
        StatusResponse updateCommentResponse = postService.deleteComment(postId, commentPostDTO);
        return new ResponseEntity(updateCommentResponse, updateCommentResponse.getResponse());
    }

}
