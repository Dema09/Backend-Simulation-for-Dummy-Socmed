package org.java.personal.project.dto.request.story;

import java.util.List;

public class StoryCollectionWhenUpdateRequestDTO{
    private String collectionId;
    private List<String> deletedStoryIdList;
    private List<String> addedStoryIdList;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public List<String> getDeletedStoryIdList() {
        return deletedStoryIdList;
    }

    public void setDeletedStoryIdList(List<String> deletedStoryIdList) {
        this.deletedStoryIdList = deletedStoryIdList;
    }

    public List<String> getAddedStoryIdList() {
        return addedStoryIdList;
    }

    public void setAddedStoryIdList(List<String> addedStoryIdList) {
        this.addedStoryIdList = addedStoryIdList;
    }
}
