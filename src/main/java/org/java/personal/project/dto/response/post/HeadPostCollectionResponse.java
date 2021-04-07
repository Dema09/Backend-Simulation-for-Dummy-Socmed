package org.java.personal.project.dto.response.post;

import java.util.List;

public class HeadPostCollectionResponse {
    private String username;
    private List<PostCollectionResponse> postCollections;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PostCollectionResponse> getPostCollections() {
        return postCollections;
    }

    public void setPostCollections(List<PostCollectionResponse> postCollections) {
        this.postCollections = postCollections;
    }
}
