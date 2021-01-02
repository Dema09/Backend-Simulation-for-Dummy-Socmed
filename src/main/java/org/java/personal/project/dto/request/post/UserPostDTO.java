package org.java.personal.project.dto.request.post;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UserPostDTO {
    private MultipartFile[] postPicture;
    private String caption;
    private List<String> mentionedPeople;

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
}
