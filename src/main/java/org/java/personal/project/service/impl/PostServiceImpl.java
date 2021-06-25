package org.java.personal.project.service.impl;

import org.java.personal.project.dao.*;
import org.java.personal.project.domain.*;
import org.java.personal.project.dto.request.post.*;
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
import java.util.Iterator;
import java.util.List;

import static org.java.personal.project.constant.AppEnum.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostOrStoryLocationRepository postOrStoryLocationRepository;
    private final PostCollectionRepository postCollectionRepository;
    private final Environment env;
    private final ConvertImageOrVideoUtil convertImageOrVideoUtil;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           UserRepository userRepository,
                           CommentRepository commentRepository,
                           PostCollectionRepository postCollectionRepository,
                           PostOrStoryLocationRepository postOrStoryLocationRepository,
                           Environment env,
                           ConvertImageOrVideoUtil convertImageOrVideoUtil) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postCollectionRepository = postCollectionRepository;
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
            return statusResponse.statusNotFound(THIS_USER_WITH_ID.getMessage() + userId + IS_NOT_EXISTS.getMessage(), null);

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
        PostOrStoryLocation currentPostOrStoryLocation = postOrStoryLocationRepository.findPostOrStoryLocationByLocationName(userPostDTO.getPostLocation().getLocationName());
        if(currentPostOrStoryLocation != null)
            return currentPostOrStoryLocation;

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
        if(mentionedPeoples == null || mentionedPeoples.size() == 0)
            return new ArrayList<>();

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
            postResponses.add(insertToPostResponse(post, postBases64));
        }

        headPostResponse.setPosts(postResponses);
        return statusResponse.statusOk(headPostResponse);
    }

    private PostResponse insertToPostResponse(Post post, List<String> postBases64) throws IOException {
        PostResponse postResponse = new PostResponse();

        postResponse.setPostBase64(convertImageOrVideoUtil.convertFileToBase64String(post.getPostPicture(), postBases64));
        postResponse.setCaption(post.getPostCaption());
        postResponse.setNumberOfLikes(post.getUserLike() == null ? 0 : post.getUserLike().size());
        postResponse.setLikes(post.getUserLike() == null ? new ArrayList<>() : insertUserLikeResponse(post.getUserLike()));
        postResponse.setComments(post.getComments() == null ? new ArrayList<>() : insertCommentResponse(post.getComments()));
        postResponse.setLocationResponse(post.getPostOrStoryLocation() == null ? new LocationHeadResponse() : insertLocationResponse(post.getPostOrStoryLocation()));

        return postResponse;
    }

    private LocationHeadResponse insertLocationResponse(PostOrStoryLocation postOrStoryLocation) {
        LocationHeadResponse locationHeadResponse = new LocationHeadResponse();
        locationHeadResponse.setLocationName(postOrStoryLocation.getLocationName());
        locationHeadResponse.setLocation(insertLocationCoordinatesResponse(postOrStoryLocation.getLocation()));

        return locationHeadResponse;
    }

    private LocationResponse insertLocationCoordinatesResponse(Location location) {
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setType(location.getType());
        locationResponse.setCoordinates(location.getCoordinates());

        return locationResponse;
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
        postResponse.setPostBase64(convertImageOrVideoUtil.convertFileToBase64String(currentPost.getPostPicture(), postBases64));
        postResponse.setCaption(currentPost.getPostCaption());
        postResponse.setNumberOfLikes(currentPost.getUserLike() == null ? 0 : currentPost.getUserLike().size());
        postResponse.setLikes(insertUserLikeResponse(currentPost.getUserLike() == null ? new ArrayList<>() : currentPost.getUserLike()));
        postResponse.setComments(currentPost.getComments() == null ? new ArrayList<>() : insertCommentResponse(currentPost.getComments()));
        postResponse.setLocationResponse(currentPost.getPostOrStoryLocation() == null ? null : insertLocationResponse(currentPost.getPostOrStoryLocation()));

        return statusResponse.statusOk(postResponse);
    }

    @Override
    public StatusResponse updateCaptionPost(UpdatePostDTO updatePostDTO, String userId) {
        StatusResponse statusResponse = new StatusResponse();
        List<DummyUser> mentionedUsers = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage() + userId, null);

        Post currentPost = postRepository.getPostByPostIdAndAndDummyUser(updatePostDTO.getPostId(), currentUser);
        if(currentPost == null)
            return statusResponse.statusNotFound(POST_NOT_FOUND.getMessage(), null);

        currentPost.setUpdated(true);
        currentPost.setPostCaption(updatePostDTO.getCaption());

        for(String mentionedUserId : updatePostDTO.getMentionedUserId()){
            DummyUser currentMentionedUser = userRepository.findOne(mentionedUserId);
            if(currentMentionedUser == null || currentMentionedUser.getId().equals(currentUser.getId()))
                continue;
            else
                mentionedUsers.add(currentMentionedUser);
        }
        currentPost.setMentionedUsers(mentionedUsers);

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

    @Override
    public StatusResponse savePostsToCollection(SavedPostToCollectionDTO savedPostToCollectionDTO, String userId) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage() + userId, null);

        Post currentPost = postRepository.findOne(savedPostToCollectionDTO.getPostId());

        if(currentPost == null)
            return statusResponse.statusNotFound(POST_NOT_FOUND.getMessage(), null);

        PostCollection checkPostCollection = postCollectionRepository.findPostCollectionByPostCollectionName(savedPostToCollectionDTO.getPostCollectionName());

        if((savedPostToCollectionDTO.getPostCollectionName().equals("") || savedPostToCollectionDTO.getPostCollectionName() == null) && checkPostCollection != null)
            savedPostToCollectionDTO.setPostCollectionName(YOUR_POST_COLLECTION.getMessage());

        if((savedPostToCollectionDTO.getPostCollectionId() == null || savedPostToCollectionDTO.getPostCollectionId().equals("") && checkPostCollection != null))
            insertIntoNewSavedPostCollectionBasedOnCurrentUser(savedPostToCollectionDTO, currentUser, currentPost);
        else
            insertIntoAnExistingPostCollectionBasedOnCurrentUser(savedPostToCollectionDTO, currentPost);

        return statusResponse.statusOk(SUCCESSFULLY_INSERT_OR_UPDATE_POST_COLLECTION.getMessage());
    }


    @Override
    public StatusResponse getUserPostCollectionByUserId(String userId) throws IOException {
        StatusResponse statusResponse = new StatusResponse();
        HeadPostCollectionResponse headPostCollectionResponse = new HeadPostCollectionResponse();
        List<PostCollectionResponse> postCollectionResponses = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);

        if(currentUser == null)
            return statusResponse.statusNotFound(THIS_USER_WITH_ID.getMessage() + userId + IS_NOT_EXISTS.getMessage(), null);

        List<PostCollection> postCollections = postCollectionRepository.findAllByDummyUser(currentUser);

        if(postCollections == null || postCollections.size() == 0)
            return statusResponse.statusOk(new ArrayList<>());

        for(PostCollection postCollection : postCollections){
            PostCollectionResponse postCollectionResponse = new PostCollectionResponse();
            postCollectionResponse.setPostCollectionId(postCollection.getPostCollectionId());
            postCollectionResponse.setPostCollectionName(postCollection.getPostCollectionName());
            postCollectionResponse.setPosts(insertPostsOneByOne(postCollection.getPosts()));

            postCollectionResponses.add(postCollectionResponse);
        }
        headPostCollectionResponse.setUsername(currentUser.getUsername());
        headPostCollectionResponse.setPostCollections(postCollectionResponses);

        return statusResponse.statusOk(headPostCollectionResponse);

    }

    @Override
    public StatusResponse getOnePostCollectionByPostCollectionId(String postCollectionId, String userId) throws IOException {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusBadRequest(USER_NOT_FOUND.getMessage() + userId, null);

        PostCollection currentPostCollection = postCollectionRepository.findPostCollectionByPostCollectionIdAndDummyUser(postCollectionId, currentUser);
        if(currentPostCollection == null)
            return statusResponse.statusOk(new PostCollection());

        PostCollectionResponse postCollectionResponse = new PostCollectionResponse();
        postCollectionResponse.setPostCollectionId(currentPostCollection.getPostCollectionId());
        postCollectionResponse.setPostCollectionName(currentPostCollection.getPostCollectionName());
        postCollectionResponse.setPosts(insertPostsOneByOne(currentPostCollection.getPosts()));

        return statusResponse.statusOk(postCollectionResponse);
    }

    @Override
    public StatusResponse updatePostCollectionByPostCollectionId(String userId, String postCollectionId, UpdatePostCollectionDTO updatePostCollectionDTO) {
        StatusResponse statusResponse = new StatusResponse();
        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage(), null);

        PostCollection currentPostCollection = postCollectionRepository.findPostCollectionByPostCollectionIdAndDummyUser(postCollectionId, currentUser);
        if(currentPostCollection == null)
            return statusResponse.statusOk(new PostCollection());

        currentPostCollection.setPostCollectionName(updatePostCollectionDTO.getPostCollectionName());
        postCollectionRepository.save(currentPostCollection);

        return statusResponse.statusOk(SUCCESSFULLY_UPDATE_POST_COLLECTION_NAME.getMessage());
    }

    @Override
    public StatusResponse updatePostCollectionContentByPostCollectionId(String userId, String postCollectionId, UpdatePostCollectionContentDTO updatePostCollectionContentDTO) {
        StatusResponse statusResponse = new StatusResponse();
        List<String> failedPostIds = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_NOT_FOUND.getMessage(), null);

        PostCollection postCollection = postCollectionRepository.findPostCollectionByPostCollectionIdAndDummyUser(postCollectionId, currentUser);
        List<Post> posts = postCollection.getPosts();
        if(postCollection == null)
            return statusResponse.statusOk(new PostCollection());

        if(updatePostCollectionContentDTO.getAddedPostCollectionIds() != null)
            insertAddedPostContentToPostCollection(postCollection, updatePostCollectionContentDTO.getAddedPostCollectionIds(), failedPostIds, posts);

        if(updatePostCollectionContentDTO.getRemovedPostCollectionIds() != null)
        removePostContentFromPostCollection(postCollection, updatePostCollectionContentDTO.getRemovedPostCollectionIds(), failedPostIds, posts);

        return statusResponse.statusOk(SUCCESSFULLY_UPDATE_YOUR_POST_COLLECTION_WITH_ID.getMessage() + postCollection.getPostCollectionId());
    }

    @Override
    public StatusResponse getTaggedPostByUserId(String userId) throws IOException {
        StatusResponse statusResponse = new StatusResponse();
        TaggedPostResponse taggedPostResponse = new TaggedPostResponse();
        List<PostResponse> postResponses = new ArrayList<>();
        List<String> postBases64 = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusBadRequest(USER_NOT_FOUND.getMessage(), null);

        List<Post> posts = postRepository.findAll();
        for(Post post : posts){
            boolean checkIfUserMentioned = checkIfUserIsMentioned(currentUser, post);
            if(checkIfUserMentioned)
                insertToTaggedPostResponse(post, postResponses, postBases64);
        }
        taggedPostResponse.setTaggedPosts(postResponses);
        return statusResponse.statusOk(taggedPostResponse);
    }

    @Override
    public StatusResponse unlikePost(String postId, String userId) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusBadRequest(USER_NOT_FOUND.getMessage() + userId, null);

        Post currentUnlikePost = postRepository.findOne(postId);
        if(currentUnlikePost == null)
            return statusResponse.statusBadRequest(POST_NOT_FOUND.getMessage(), null);

        Iterator<DummyUser> dummyUserIterator = currentUnlikePost.getUserLike().iterator();
        while(dummyUserIterator.hasNext()){
            DummyUser userWhoWantToRemove = dummyUserIterator.next();
            if(userWhoWantToRemove.getId().equals(currentUser.getId()))
                dummyUserIterator.remove();
        }
        return statusResponse.statusOk(SUCCESSFULLY_UNLIKE_POST.getMessage());
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

    private void insertToTaggedPostResponse(Post post, List<PostResponse> postResponses, List<String> postBases64) throws IOException {
        PostResponse currentPostResponse = insertToPostResponse(post, postBases64);
        postResponses.add(currentPostResponse);
    }

    private boolean checkIfUserIsMentioned(DummyUser currentUser, Post post) {
        boolean isUserMentioned = false;
        for(DummyUser dummyUser : post.getMentionedUsers()){
            if(dummyUser.getId().equals(currentUser.getId())) {
                isUserMentioned = true;
                break;
            }
        }
        return isUserMentioned;
    }

    private void removePostContentFromPostCollection(PostCollection postCollection, List<String> removedPostCollectionIds, List<String> failedPostIds, List<Post> posts) {
        for(String currentPostId : removedPostCollectionIds){
            Post currentPost = postRepository.findOne(currentPostId);
            if(currentPost == null)
                failedPostIds.add(currentPostId);
            else {
                Iterator<Post> postIterator = postCollection.getPosts().iterator();
                while(postIterator.hasNext()){
                    Post postWhichWantToRemove = postIterator.next();
                    if(postWhichWantToRemove.getPostId().equals(currentPost.getPostId()))
                        postIterator.remove();
                }
                postCollection.setPosts(posts);
            }
        }
        postCollectionRepository.save(postCollection);
    }

    private void insertAddedPostContentToPostCollection(PostCollection postCollection, List<String> addedPostIds, List<String> failedPostIds, List<Post> posts) {
        for(String currentPostId : addedPostIds){
            Post currentPost = postRepository.findOne(currentPostId);
            if(currentPost == null)
                failedPostIds.add(currentPostId);
            else{
             posts.add(currentPost);
             postCollection.setPosts(posts);
            }
        }
        postCollectionRepository.save(postCollection);
    }

    private List<PostResponse> insertPostsOneByOne(List<Post> posts) throws IOException {
        List<PostResponse> postResponses = new ArrayList<>();
        List<String> postBases64 = new ArrayList<>();

        for(Post post : posts){
            postResponses.add(insertToPostResponse(post, postBases64));
        }
        return postResponses;
    }

    private void insertIntoAnExistingPostCollectionBasedOnCurrentUser(SavedPostToCollectionDTO savedPostToCollectionDTO, Post currentPost) {
        PostCollection currentPostCollection = postCollectionRepository.findOne(savedPostToCollectionDTO.getPostId());
        List<Post> posts = currentPostCollection.getPosts();

        posts.add(currentPost);
        currentPostCollection.setPosts(posts);

        postCollectionRepository.save(currentPostCollection);
    }

    private void insertIntoNewSavedPostCollectionBasedOnCurrentUser(SavedPostToCollectionDTO savedPostToCollectionDTO, DummyUser currentUser, Post currentPost) {
        PostCollection postCollection = new PostCollection();
        List<Post> posts = new ArrayList<>();
        posts.add(currentPost);

        postCollection.setPostCollectionName(savedPostToCollectionDTO.getPostCollectionName());
        postCollection.setPosts(posts);
        postCollection.setDummyUser(currentUser);

        postCollectionRepository.save(postCollection);
    }
}
