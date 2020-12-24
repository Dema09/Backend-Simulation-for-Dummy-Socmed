package org.java.personal.project.dto.response.story;

import java.util.List;

public class StoryResponse {
    private String storyId;
    private String username;
    private String storyFileBase64;
    private List<String> mentionedUsers;

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStoryFileBase64() {
        return storyFileBase64;
    }

    public void setStoryFileBase64(String storyFileBase64) {
        this.storyFileBase64 = storyFileBase64;
    }

    public List<String> getMentionedUsers() {
        return mentionedUsers;
    }

    public void setMentionedUsers(List<String> mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
    }
}
