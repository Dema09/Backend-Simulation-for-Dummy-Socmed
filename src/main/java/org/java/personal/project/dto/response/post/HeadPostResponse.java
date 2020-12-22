package org.java.personal.project.dto.response.post;

import java.util.List;

public class HeadPostResponse {
    private List<PostResponse> posts;

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }
}
