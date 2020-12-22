package org.java.personal.project.dto.response.post;

import java.util.List;

public class PostResponse {
    private List<String> postBase64;
    private String caption;
    private Integer numberOfLikes;
    private List<UserLikeResponse> likes;
    private List<CommentResponseDTO> comments;

    public List<String> getPostBase64() {
        return postBase64;
    }

    public void setPostBase64(List<String> postBase64) {
        this.postBase64 = postBase64;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<UserLikeResponse> getLikes() {
        return likes;
    }

    public void setLikes(List<UserLikeResponse> likes) {
        this.likes = likes;
    }

    public Integer getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Integer numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public List<CommentResponseDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponseDTO> comments) {
        this.comments = comments;
    }
}
