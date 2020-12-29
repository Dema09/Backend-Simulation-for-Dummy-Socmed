package org.java.personal.project.dto.request.story;

import java.util.List;

public class StoryCollectionRequestDTO {
    private String collectionName;
    private List<String> storiesId;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public List<String> getStories() {
        return storiesId;
    }

    public void setStories(List<String> storiesId) {
        this.storiesId = storiesId;
    }
}
