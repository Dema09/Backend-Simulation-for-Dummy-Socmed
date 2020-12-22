package org.java.personal.project.dto.request.story;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class StoryRequestDTO {
    private MultipartFile storyPost;
    private List<String> mentionUsers;

    public MultipartFile getStoryPost() {
        return storyPost;
    }

    public void setStoryPost(MultipartFile storyPost) {
        this.storyPost = storyPost;
    }

    public List<String> getMentionUsers() {
        return mentionUsers;
    }

    public void setMentionUsers(List<String> mentionUsers) {
        this.mentionUsers = mentionUsers;
    }
}
