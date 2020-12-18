package org.java.personal.project.service.impl;

import org.java.personal.project.constant.MediaFormatEnum;
import org.java.personal.project.dao.CommentRepository;
import org.java.personal.project.dao.PostRepository;
import org.java.personal.project.dao.UserRepository;
import org.java.personal.project.domain.Comment;
import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.Post;
import org.java.personal.project.dto.request.CommentPostDTO;
import org.java.personal.project.dto.request.UpdatePostDTO;
import org.java.personal.project.dto.request.UserPostDTO;
import org.java.personal.project.dto.response.*;
import org.java.personal.project.service.PostService;
import org.java.personal.project.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.java.personal.project.constant.AppEnum.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final Environment env;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, Environment env) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.env = env;
    }

    @Override
    public StatusResponse postPictureFromUser(UserPostDTO userPostDTO, String userId) throws Exception {
        StatusResponse statusResponse = new StatusResponse();
        List<String> postCollections = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(YOUR_USERNAME_WITH_ID + userId + IS_NOT_EXISTS.getMessage(), null);

        Post currentPost = new Post();
        for(MultipartFile file : userPostDTO.getPostPicture()){
            currentPost.setPostPicture(convertImage(file.getBytes(), file, postCollections));
        }

        currentPost.setPostCaption(userPostDTO.getCaption());
        currentPost.setDummyUser(currentUser);

        postRepository.save(currentPost);
        return statusResponse.statusCreated(POST_HAS_BEEN_CREATED.getMessage(), currentPost);
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
            postResponse.setPostBase64(convertImageToBase64String(post.getPostPicture(), postBases64));
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
        postResponse.setPostBase64(convertImageToBase64String(currentPost.getPostPicture(), postBases64));
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

    private List<String> convertImageToBase64String(List<String> postPictures, List<String> postBases64) throws IOException {

        for(String postPicture : postPictures){
            File currentPostFile = new File(env.getProperty("postPicturePath") + postPicture);
            if(currentPostFile == null)
                return new ArrayList<>();

            byte[] postFileByte = Files.readAllBytes(currentPostFile.toPath().toAbsolutePath());
            String postFileInString = Base64.getEncoder().encodeToString(postFileByte);
            postBases64.add(postFileInString);
        }
        return postBases64;
    }

    private List<String> convertImage(byte[] data, MultipartFile file, List<String> postCollections) throws Exception {
        ValidationUtil validationUtil = new ValidationUtil();
        File postFile = new File(env.getProperty("postPicturePath") + file.getOriginalFilename());
        String substringPost = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));

        if(!validationUtil.validateFileFormat(substringPost))
            throw new IOException(INVALID_POST_FORMAT.getMessage());


        InputStream inputStream = file.getInputStream();

        int read = 0;
        if(!postFile.exists()) postFile.createNewFile();
        OutputStream outputStream = new FileOutputStream(postFile);
        while((read = inputStream.read(data)) != -1){
            outputStream.write(data, 0, read);
        }

        if(validationUtil.isVideo(substringPost))
            convertVideoToMp4(file, postFile);

        file.transferTo(postFile);
        postFile.getAbsolutePath();

        postCollections.add(file.getOriginalFilename());

        return postCollections;
    }

    private void convertVideoToMp4(MultipartFile file, File postFile) throws Exception {
        File targetFile = new File(env.getProperty("postPicturePath") +
                file.getOriginalFilename().replace(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")),MediaFormatEnum.MP4.getMessage()));

        AudioAttributes audio = new AudioAttributes();
        audio.setBitRate(64000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        video.setX264Profile(VideoAttributes.X264_PROFILE.BASELINE);
        video.setBitRate(160000);
        video.setFrameRate(15);
        video.setSize(new VideoSize(400,300));

        EncodingAttributes attributes = new EncodingAttributes();
        attributes.setFormat("mp4");
        attributes.setAudioAttributes(audio);
        attributes.setVideoAttributes(video);

        try{
            ws.schild.jave.Encoder encoder = new Encoder();
            encoder.encode((List<MultimediaObject>) postFile, targetFile, attributes);
        }catch(Exception e){
            throw new Exception(CAN_NOT_ENCODE_THE_VIDEO.getMessage(),e);
        }

    }

}
