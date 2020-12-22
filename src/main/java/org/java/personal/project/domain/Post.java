package org.java.personal.project.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "user_post")
public class Post implements Persistable<String> {
    @Id
    private String postId;

    private List<String> postPicture;
    private String postCaption;
    private boolean isUpdated;
    private List<DummyUser> userLike;
    private List<Comment> comments;
    private boolean persisted;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date lastModified;


    @DBRef
    private DummyUser dummyUser;

    public Post() {
    }

    public Post(String postCaption, DummyUser dummyUser) {
        this.postCaption = postCaption;
        this.dummyUser = dummyUser;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<String> getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(List<String> postPicture) {
        this.postPicture = postPicture;
    }

    public String getPostCaption() {
        return postCaption;
    }

    public void setPostCaption(String postCaption) {
        this.postCaption = postCaption;
    }

    public List<DummyUser> getUserLike() {
        return userLike;
    }

    public void setUserLike(List<DummyUser> userLike) {
        this.userLike = userLike;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public void setComments(List<Comment > comments) {
        this.comments = comments;
    }

    public DummyUser getDummyUser() {
        return dummyUser;
    }

    public void setDummyUser(DummyUser dummyUser) {
        this.dummyUser = dummyUser;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String getId() {
        return postId;
    }

    @Override
    public boolean isNew() {
        return persisted;
    }

    public void setPersisted(boolean persisted) {
        this.persisted = persisted;
    }
}
