package org.java.personal.project.dto.response.post;

import java.util.List;

public class TaggedPostResponse {
    private List<PostResponse> taggedPosts;

    public List<PostResponse> getTaggedPosts() {
        return taggedPosts;
    }

    public void setTaggedPosts(List<PostResponse> taggedPosts) {
        this.taggedPosts = taggedPosts;
    }
}
