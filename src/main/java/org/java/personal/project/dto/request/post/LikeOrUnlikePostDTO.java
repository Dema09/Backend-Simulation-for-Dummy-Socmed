package org.java.personal.project.dto.request.post;

public class LikeOrUnlikePostDTO {
    private String postId;
    private String userId;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
