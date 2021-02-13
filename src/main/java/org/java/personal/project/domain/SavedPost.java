package org.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "saved_post")
public class SavedPost {

    @Id
    private String savedPostId;

    private String postCollectionName;

    @DBRef
    private List<Post> posts;

    @DBRef
    private DummyUser dummyUser;

    public String getSavedPostId() {
        return savedPostId;
    }

    public void setSavedPostId(String savedPostId) {
        this.savedPostId = savedPostId;
    }

    public String getPostCollectionName() {
        return postCollectionName;
    }

    public void setPostCollectionName(String postCollectionName) {
        this.postCollectionName = postCollectionName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public DummyUser getDummyUser() {
        return dummyUser;
    }

    public void setDummyUser(DummyUser dummyUser) {
        this.dummyUser = dummyUser;
    }
}
