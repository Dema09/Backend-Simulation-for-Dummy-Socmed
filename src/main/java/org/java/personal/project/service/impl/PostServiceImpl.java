package org.java.personal.project.service.impl;

import org.java.personal.project.constant.AppEnum;
import org.java.personal.project.dao.PostRepository;
import org.java.personal.project.dao.UserRepository;
import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.Post;
import org.java.personal.project.dto.request.UserPostDTO;
import org.java.personal.project.dto.response.StatusResponse;
import org.java.personal.project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Environment env;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, Environment env) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.env = env;
    }

    @Override
    public StatusResponse postPictureFromUser(UserPostDTO userPostDTO, String userId) throws IOException {
        StatusResponse statusResponse = new StatusResponse();
        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(AppEnum.YOUR_USERNAME_WITH_ID + userId + AppEnum.IS_NOT_EXISTS.getMessage(), null);

        Post currentPost = new Post(
                convertImage(userPostDTO.getPostPicture().getBytes(), userPostDTO.getPostPicture()),
                userPostDTO.getCaption(),
                currentUser
        );
        postRepository.save(currentPost);
        return statusResponse.statusCreated(AppEnum.POST_HAS_BEEN_CREATED.getMessage(), currentPost);
    }

    @Override
    public StatusResponse getUserPostById(String userId) {
        return null;
    }

    private String convertImage(byte[] data, MultipartFile file) throws IOException {

        File postFile = new File(env.getProperty("postPicturePath") + file.getOriginalFilename());
        InputStream inputStream = file.getInputStream();

        int read = 0;
        if(!postFile.exists()) postFile.createNewFile();
        OutputStream outputStream = new FileOutputStream(postFile);
        while((read = inputStream.read(data)) != -1){
            outputStream.write(data, 0, read);
        }
        file.transferTo(postFile);
        postFile.getAbsolutePath();

        return file.getOriginalFilename();
    }

}
