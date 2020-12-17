package org.java.personal.project.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class UserPostDTO {
    private MultipartFile[] postPicture;
    private String caption;

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

}
