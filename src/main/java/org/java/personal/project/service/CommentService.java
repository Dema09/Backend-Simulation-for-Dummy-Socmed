package org.java.personal.project.service;

import org.java.personal.project.dto.request.post.CommentPostDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    StatusResponse commentToUserPost(CommentPostDTO commentPostDTO, String postId);

    StatusResponse deleteComment(String postId, CommentPostDTO commentPostDTO);
}
