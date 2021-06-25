package org.java.personal.project.controller;

import org.java.personal.project.dto.request.post.CommentPostDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/{postId}")
    public ResponseEntity commentToOtherUser(@PathVariable String postId, @RequestBody CommentPostDTO commentPostDTO){
        StatusResponse commentResponse = commentService.commentToUserPost(commentPostDTO, postId);
        return new ResponseEntity(commentResponse, commentResponse.getResponse());
    }

    @DeleteMapping("/deleteComment/{postId}")
    public ResponseEntity deleteComment(@PathVariable String postId, @RequestBody CommentPostDTO commentPostDTO){
        StatusResponse updateCommentResponse = commentService.deleteComment(postId, commentPostDTO);
        return new ResponseEntity(updateCommentResponse, updateCommentResponse.getResponse());
    }
}
