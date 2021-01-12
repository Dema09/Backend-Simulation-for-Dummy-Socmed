package org.java.personal.project.dto.request.post;

import org.java.personal.project.dto.request.PostOrStoryLocationDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UserPostDTO {
    private MultipartFile[] postPicture;
    private String caption;
    private List<String> mentionedPeople;
    private PostOrStoryLocationDTO postLocation;

    public MultipartFile[] getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(MultipartFile[] postPicture) {
        this.postPicture = postPicture;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<String> getMentionedPeople() {
        return mentionedPeople;
    }

    public void setMentionedPeople(List<String> mentionedPeople) {
        this.mentionedPeople = mentionedPeople;
    }

    public PostOrStoryLocationDTO getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(PostOrStoryLocationDTO postLocation) {
        this.postLocation = postLocation;
    }
}
