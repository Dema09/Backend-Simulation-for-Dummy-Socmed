package org.java.personal.project.dto.request.post;

import java.util.List;

public class UpdatePostCollectionContentDTO {
    private List<String> addedPostCollectionIds;
    private List<String> removedPostCollectionIds;

    public List<String> getAddedPostCollectionIds() {
        return addedPostCollectionIds;
    }

    public void setAddedPostCollectionIds(List<String> addedPostCollectionIds) {
        this.addedPostCollectionIds = addedPostCollectionIds;
    }

    public List<String> getRemovedPostCollectionIds() {
        return removedPostCollectionIds;
    }

    public void setRemovedPostCollectionIds(List<String> removedPostCollectionIds) {
        this.removedPostCollectionIds = removedPostCollectionIds;
    }
}
