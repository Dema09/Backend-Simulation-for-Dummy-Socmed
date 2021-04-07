package org.java.personal.project.dto.request.post;

public class SavedPostToCollectionDTO {
    private String postCollectionId;
    private String postId;
    private String postCollectionName;

    public String getPostCollectionId() {
        return postCollectionId;
    }

    public void setPostCollectionId(String postCollectionId) {
        this.postCollectionId = postCollectionId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostCollectionName() {
        return postCollectionName;
    }

    public void setPostCollectionName(String postCollectionName) {
        this.postCollectionName = postCollectionName;
    }
}
