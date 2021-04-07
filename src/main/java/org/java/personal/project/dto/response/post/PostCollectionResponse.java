package org.java.personal.project.dto.response.post;

import java.util.List;

public class PostCollectionResponse {
    private String postCollectionId;
    private String postCollectionName;
    private List<PostResponse> posts;

    public String getPostCollectionId() {
        return postCollectionId;
    }

    public void setPostCollectionId(String postCollectionId) {
        this.postCollectionId = postCollectionId;
    }

    public String getPostCollectionName() {
        return postCollectionName;
    }

    public void setPostCollectionName(String postCollectionName) {
        this.postCollectionName = postCollectionName;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }
}
