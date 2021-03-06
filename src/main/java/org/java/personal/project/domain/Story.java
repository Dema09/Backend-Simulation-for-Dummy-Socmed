package org.java.personal.project.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "user_story")
public class Story {
    @Id
    private String storyId;

    private String storyFileName;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @DBRef
    private DummyUser currentUserStory;

    @DBRef
    private List<DummyUser> mentionPeople;

    @DBRef
    private PostOrStoryLocation storyLocation;

    private boolean isCloseFriendMode;

    public Story() {
    }


    public String getStoryId() {
        return storyId;
    }

    public String getStoryFileName() {
        return storyFileName;
    }

    public void setStoryFileName(String storyFileName) {
        this.storyFileName = storyFileName;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DummyUser getCurrentUserStory() {
        return currentUserStory;
    }

    public void setCurrentUserStory(DummyUser currentUserStory) {
        this.currentUserStory = currentUserStory;
    }

    public List<DummyUser> getMentionPeople() {
        return mentionPeople;
    }

    public void setMentionPeople(List<DummyUser> mentionPeople) {
        this.mentionPeople = mentionPeople;
    }

    public PostOrStoryLocation getStoryLocation() {
        return storyLocation;
    }

    public void setStoryLocation(PostOrStoryLocation storyLocation) {
        this.storyLocation = storyLocation;
    }

    public boolean isCloseFriendMode() {
        return isCloseFriendMode;
    }

    public void setCloseFriendMode(boolean closeFriendMode) {
        isCloseFriendMode = closeFriendMode;
    }
}
