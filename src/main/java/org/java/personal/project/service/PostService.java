package org.java.personal.project.service;

import org.java.personal.project.dto.request.CommentPostDTO;
import org.java.personal.project.dto.request.UserPostDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PostService {
    StatusResponse postPictureFromUser(UserPostDTO userPostDTO, String userId) throws Exception;

    StatusResponse getUserPostById(String userId) throws IOException;

    StatusResponse commentToUserPost(CommentPostDTO commentPostDTO, String postId);

    StatusResponse likePost(String postId, String userId);

}
