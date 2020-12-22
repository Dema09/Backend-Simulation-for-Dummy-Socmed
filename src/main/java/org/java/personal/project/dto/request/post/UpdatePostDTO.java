package org.java.personal.project.dto.request.post;

public class UpdatePostDTO {
    private String postId;
    private String caption;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
