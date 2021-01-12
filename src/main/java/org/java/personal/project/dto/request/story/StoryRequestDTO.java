package org.java.personal.project.dto.request.story;

import org.java.personal.project.dto.request.PostOrStoryLocationDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class StoryRequestDTO {
    private MultipartFile storyPost;
    private List<String> mentionUsers;
    private PostOrStoryLocationDTO storyLocation;

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

    public PostOrStoryLocationDTO getStoryLocation() {
        return storyLocation;
    }

    public void setStoryLocation(PostOrStoryLocationDTO storyLocation) {
        this.storyLocation = storyLocation;
    }
}
