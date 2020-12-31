package org.java.personal.project.dto.response.story;

import java.util.List;

public class StoryResponseAfterUpdate {
    private String collectionId;
    private List<String> failedCollectionId;
    private String message;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public List<String> getFailedCollectionId() {
        return failedCollectionId;
    }

    public void setFailedCollectionId(List<String> failedCollectionId) {
        this.failedCollectionId = failedCollectionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
