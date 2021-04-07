package org.java.personal.project.dto.request.post;

import java.util.List;

public class UpdatePostDTO {
    private String postId;
    private String caption;
    private List<String> mentionedUserId;

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

    public List<String> getMentionedUserId() {
        return mentionedUserId;
    }

    public void setMentionedUserId(List<String> mentionedUserId) {
        this.mentionedUserId = mentionedUserId;
    }
}
