package org.java.personal.project.service.impl;

import org.java.personal.project.repository.CommentRepository;
import org.java.personal.project.repository.PostRepository;
import org.java.personal.project.repository.UserRepository;
import org.java.personal.project.domain.Comment;
import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.Post;
import org.java.personal.project.dto.request.post.CommentPostDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.java.personal.project.enumeration.AppEnum.*;

@Service
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public StatusResponse commentToUserPost(CommentPostDTO commentPostDTO, String postId) {
        StatusResponse statusResponse = new StatusResponse();

        Post currentPost = postRepository.findOne(postId);
        if(currentPost == null)
            return statusResponse.statusNotFound(POST_NOT_FOUND.getMessage(), null);

        DummyUser currentUser = userRepository.findOne(commentPostDTO.getUserId());
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage() + commentPostDTO.getUserId(), null);

        Comment comment = new Comment();
        comment.setComment(commentPostDTO.getComment());
        comment.setAuthor(currentUser);
        comment.setPost(currentPost);

        commentRepository.save(comment);
        return statusResponse.statusCreated(SUCCESSFULLY_ADD_COMMENT.getMessage(), comment.getCommentId());
    }

    @Override
    public StatusResponse deleteComment(String postId, CommentPostDTO commentPostDTO) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(commentPostDTO.getUserId());
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage(), null);

        Post currentPost = postRepository.findOne(postId);
        if(currentPost == null)
            return statusResponse.statusNotFound(POST_NOT_FOUND.getMessage(), null);

        return null;

//        Iterator<Comment> commentIterator = currentPost.getComments().iterator();
//        while(commentIterator.hasNext()){
//            DummyUser userWhoWan = commentIterator.next().getAuthor();
//
//        }
    }
}
