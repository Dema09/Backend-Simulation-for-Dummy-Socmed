package org.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user_post")
public class Post {
    @Id
    private String postId;

    private String postPicture;
    private String postCaption;
    private List<DummyUser> userLike;
    private List<Comment> comments;

    @DBRef
    private DummyUser dummyUser;

    public Post() {
    }

    public Post(String postPicture, String postCaption, DummyUser dummyUser) {
        this.postPicture = postPicture;
        this.postCaption = postCaption;
        this.dummyUser = dummyUser;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(String postPicture) {
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

    public void setComments(List<Comment > comments) {
        this.comments = comments;
    }

    public DummyUser getDummyUser() {
        return dummyUser;
    }

    public void setDummyUser(DummyUser dummyUser) {
        this.dummyUser = dummyUser;
    }
}
