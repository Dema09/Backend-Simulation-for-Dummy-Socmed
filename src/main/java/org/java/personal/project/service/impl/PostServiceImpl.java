package org.java.personal.project.service.impl;

import org.java.personal.project.dao.CommentRepository;
import org.java.personal.project.dao.PostOrStoryLocationRepository;
import org.java.personal.project.dao.PostRepository;
import org.java.personal.project.dao.UserRepository;
import org.java.personal.project.domain.*;
import org.java.personal.project.dto.request.post.CommentPostDTO;
import org.java.personal.project.dto.request.post.UpdatePostDTO;
import org.java.personal.project.dto.request.post.UserPostDTO;
import org.java.personal.project.dto.response.*;
import org.java.personal.project.dto.response.post.*;
import org.java.personal.project.service.PostService;
import org.java.personal.project.util.ConvertImageOrVideoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.java.personal.project.constant.AppEnum.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostOrStoryLocationRepository postOrStoryLocationRepository;
    private final Environment env;
    private final ConvertImageOrVideoUtil convertImageOrVideoUtil;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           UserRepository userRepository,
                           CommentRepository commentRepository,
                           PostOrStoryLocationRepository postOrStoryLocationRepository,
                           Environment env,
                           ConvertImageOrVideoUtil convertImageOrVideoUtil) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postOrStoryLocationRepository = postOrStoryLocationRepository;
        this.env = env;
        this.convertImageOrVideoUtil = convertImageOrVideoUtil;
    }

    @Override
    public StatusResponse postPictureFromUser(UserPostDTO userPostDTO, String userId) throws Exception {
        StatusResponse statusResponse = new StatusResponse();
        PostAfterInsertResponse postAfterInsertResponse = new PostAfterInsertResponse();

        List<String> postCollections = new ArrayList<>();
        List<String> failedMentionedPeople = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(YOUR_USERNAME_WITH_ID + userId + IS_NOT_EXISTS.getMessage(), null);

        Post currentPost = new Post();
        for(MultipartFile file : userPostDTO.getPostPicture()){
            currentPost.setPostPicture(convertImageOrVideoUtil.convertImage(file.getBytes(), file, postCollections));
        }

        currentPost.setPostCaption(userPostDTO.getCaption());
        currentPost.setDummyUser(currentUser);
        currentPost.setMentionedUsers(insertMentionedUsers(userPostDTO.getMentionedPeople(), failedMentionedPeople, currentUser));
        currentPost.setPersisted(true);

        if(userPostDTO.getPostLocation() != null)
        currentPost.setPostOrStoryLocation(setPostOrStoryLocation(userPostDTO));

        postRepository.save(currentPost);

        postAfterInsertResponse.setPostId(currentPost.getPostId());
        postAfterInsertResponse.setMessage(POST_HAS_BEEN_CREATED.getMessage());
        postAfterInsertResponse.setFailedMentionedUsers(failedMentionedPeople);

        return statusResponse.statusCreated(postAfterInsertResponse.getMessage(), postAfterInsertResponse);
    }

    private PostOrStoryLocation setPostOrStoryLocation(UserPostDTO userPostDTO) {
        PostOrStoryLocation postOrStoryLocation = new PostOrStoryLocation();
        postOrStoryLocation.setLocationName(userPostDTO.getPostLocation().getLocationName());
        postOrStoryLocation.setLocation(setLocationCoordinates(userPostDTO));

        postOrStoryLocationRepository.save(postOrStoryLocation);
        return postOrStoryLocation;
    }

    private Location setLocationCoordinates(UserPostDTO userPostDTO) {
        Location location = new Location(POINT.getMessage(),
                userPostDTO.getPostLocation().getLongitude(),
                userPostDTO.getPostLocation().getLatitude());

        return location;
    }

    private List<DummyUser> insertMentionedUsers(List<String> mentionedPeoples, List<String> failedMentionedUsers, DummyUser currentUser){
        List<DummyUser> dummyUsers = new ArrayList<>();
        for(String mentionedPeople : mentionedPeoples){
            DummyUser currentMentionedUser = userRepository.findOne(mentionedPeople);
            if(currentMentionedUser == null || mentionedPeople.equals(currentUser.getId())) {
                failedMentionedUsers.add(mentionedPeople);
            }else{
                dummyUsers.add(currentMentionedUser);
            }
        }
        return dummyUsers;
    }

    @Override
    public StatusResponse getUserPostById(String userId) throws IOException {
        StatusResponse statusResponse = new StatusResponse();
        HeadPostResponse headPostResponse = new HeadPostResponse();
        List<PostResponse> postResponses = new ArrayList<>();
        List<String> postBases64 = new ArrayList<>();

        DummyUser dummyUser = userRepository.findOne(userId);
        List<Post> currentPostByUser = postRepository.getAllByDummyUser(dummyUser);

        for(Post post : currentPostByUser){
            PostResponse postResponse = new PostResponse();
            postResponse.setPostBase64(convertImageOrVideoUtil.convertImageToBase64String(post.getPostPicture(), postBases64));
            postResponse.setCaption(post.getPostCaption());
            postResponse.setNumberOfLikes(post.getUserLike() == null ? 0 : post.getUserLike().size());
            postResponse.setLikes(post.getUserLike() == null ? new ArrayList<>() : insertUserLikeResponse(post.getUserLike()));
            postResponse.setComments(post.getComments() == null ? new ArrayList<>() : insertCommentResponse(post.getComments()));

            postResponses.add(postResponse);
        }
        headPostResponse.setPosts(postResponses);
        return statusResponse.statusOk(headPostResponse);

    }

    private List<UserLikeResponse> insertUserLikeResponse(List<DummyUser> userLikes) {
        List<UserLikeResponse> userLikeResponses = new ArrayList<>();
        for(DummyUser likedUser : userLikes){
            UserLikeResponse userLikeResponse = new UserLikeResponse();
            userLikeResponse.setUserId(likedUser.getId());
            userLikeResponse.setUsername(likedUser.getUsername());

            userLikeResponses.add(userLikeResponse);
        }
        return userLikeResponses;
    }

    private List<CommentResponseDTO> insertCommentResponse(List<Comment> comments) {
        List<CommentResponseDTO> commentResponseDTOS = new ArrayList<>();

        for(Comment comment : comments){
            CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
            commentResponseDTO.setComment(comment.getComment());
            commentResponseDTO.setAuthor(comment.getAuthor().getUsername());

            commentResponseDTOS.add(commentResponseDTO);
        }
        return commentResponseDTOS;
    }

    @Override
    public StatusResponse commentToUserPost(CommentPostDTO commentPostDTO, String postId) {
        StatusResponse statusResponse = new StatusResponse();
        List<Comment> comments = new ArrayList<>();

        Post currentPost = postRepository.findOne(postId);
        if(currentPost == null)
            return statusResponse.statusNotFound(POST_NOT_FOUND.getMessage(), null);

        DummyUser currentUser = userRepository.findOne(commentPostDTO.getUserId());
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage() + commentPostDTO.getUserId(), null);

        Comment comment = new Comment();
        comment.setComment(commentPostDTO.getComment());
        comment.setAuthor(currentUser);
        comments.add(comment);

        currentPost.setComments(comments);
        postRepository.save(currentPost);

        return statusResponse.statusOk(SUCCESSFULLY_ADD_COMMENT.getMessage());
    }

    @Override
    public StatusResponse likePost(String postId, String userId) {
        StatusResponse statusResponse = new StatusResponse();
        Post currentPost = postRepository.findOne(postId);
        if(currentPost == null)
            return statusResponse.statusNotFound(POST_NOT_FOUND.getMessage(), null);

        DummyUser currentUser = userRepository.findOne(userId);
        if (currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage() + userId, null);

        List<DummyUser> dummyUsers = currentPost.getUserLike();
        if(dummyUsers == null)
            dummyUsers = new ArrayList<>();

        dummyUsers.add(currentUser);

        currentPost.setUserLike(dummyUsers);
        postRepository.save(currentPost);

        return statusResponse.statusOk(POST_WITH_ID.getMessage() + postId + LIKED_BY.getMessage() + currentUser.getUsername());
    }

    @Override
    public StatusResponse getOnePostByUserId(String postId, String userId) throws IOException {
        StatusResponse statusResponse = new StatusResponse();
        List<String> postBases64 = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage() + userId, null);

        Post currentPost = postRepository.getPostByPostIdAndAndDummyUser(postId, currentUser);
        if(currentPost == null)
            return statusResponse.statusNotFound(POST_NOT_FOUND.getMessage(), null);

        PostResponse postResponse = new PostResponse();
        postResponse.setPostBase64(convertImageOrVideoUtil.convertImageToBase64String(currentPost.getPostPicture(), postBases64));
        postResponse.setCaption(currentPost.getPostCaption());
        postResponse.setNumberOfLikes(currentPost.getUserLike().size());
        postResponse.setLikes(insertUserLikeResponse(currentPost.getUserLike()));
        postResponse.setComments(insertCommentResponse(currentPost.getComments()));

        return statusResponse.statusOk(postResponse);
    }

    @Override
    public StatusResponse updateCaptionPost(UpdatePostDTO updatePostDTO, String userId) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage() + userId, null);

        Post currentPost = postRepository.getPostByPostIdAndAndDummyUser(updatePostDTO.getPostId(), currentUser);
        if(currentPost == null)
            return statusResponse.statusNotFound(POST_NOT_FOUND.getMessage(), null);

        currentPost.setUpdated(true);
        currentPost.setPostCaption(updatePostDTO.getCaption());

        postRepository.save(currentPost);
        return statusResponse.statusOk(UPDATE_CAPTION_SUCCESSFULLY.getMessage() + currentPost.getPostId());
    }

    @Override
    public StatusResponse deleteUserPostByPostId(String postId, String userId) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage() + userId, null);

        Post currentPost = postRepository.getPostByPostIdAndAndDummyUser(postId, currentUser);
        if(currentPost == null)
            return statusResponse.statusNotFound(POST_NOT_FOUND.getMessage(), null);

        postRepository.delete(currentPost);
        return statusResponse.statusOk(DELETE_POST_SUCCESSFULLY.getMessage() + postId);
    }
}
