package org.java.personal.project.dto.response.post;

import java.util.List;

public class PostAfterInsertResponse {
    private String postId;
    private List<String> failedMentionedUsers;
    private String message;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<String> getFailedMentionedUsers() {
        return failedMentionedUsers;
    }

    public void setFailedMentionedUsers(List<String> failedMentionedUsers) {
        this.failedMentionedUsers = failedMentionedUsers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
