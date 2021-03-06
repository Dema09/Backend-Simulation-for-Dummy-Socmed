package org.java.personal.project.service;

import org.java.personal.project.dto.request.post.*;
import org.java.personal.project.dto.response.StatusResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PostService {
    StatusResponse postPictureFromUser(UserPostDTO userPostDTO, String userId) throws Exception;

    StatusResponse getUserPostById(String userId) throws IOException;

    StatusResponse likePost(String postId, String userId);

    StatusResponse getOnePostByUserId(String postId,String userId) throws IOException;

    StatusResponse updateCaptionPost(UpdatePostDTO updatePostDTO, String userId);

    StatusResponse deleteUserPostByPostId(String postId, String userId);

    StatusResponse savePostsToCollection(SavedPostToCollectionDTO savedPostToCollectionDTO, String userId);

    StatusResponse getUserPostCollectionByUserId(String userId) throws IOException;

    StatusResponse getOnePostCollectionByPostCollectionId(String postCollectionId, String userId) throws IOException;

    StatusResponse updatePostCollectionByPostCollectionId(String userId, String postCollectionId, UpdatePostCollectionDTO updatePostCollectionDTO);

    StatusResponse updatePostCollectionContentByPostCollectionId(String userId, String postCollectionId, UpdatePostCollectionContentDTO updatePostCollectionContentDTO);

    StatusResponse getTaggedPostByUserId(String userId) throws IOException;

    StatusResponse unlikePost(String postId, String userId);
}
