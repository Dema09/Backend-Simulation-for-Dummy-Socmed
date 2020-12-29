package org.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "story_collection")
public class StoryCollection {

    @Id
    private String collectionId;

    private String collectionName;

    @DBRef
    private DummyUser dummyUser;

    @DBRef
    private List<Story> stories;

    public StoryCollection() {
    }

    public StoryCollection(String collectionName, DummyUser dummyUser, List<Story> stories) {
        this.collectionName = collectionName;
        this.dummyUser = dummyUser;
        this.stories = stories;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public DummyUser getDummyUser() {
        return dummyUser;
    }

    public void setDummyUser(DummyUser dummyUser) {
        this.dummyUser = dummyUser;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
