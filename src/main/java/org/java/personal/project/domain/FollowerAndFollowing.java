package org.java.personal.project.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "follower_and_following")
public class FollowerAndFollowing {
    @Id
    private String followerAndFollowingId;

    @DBRef
    private DummyUser dummyUser;

    private List<DummyUser> followers;

    private List<DummyUser> followings;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public FollowerAndFollowing() {
    }

    public FollowerAndFollowing(String followerAndFollowingId, DummyUser dummyUser, List<DummyUser> followers, List<DummyUser> followings) {
        this.followerAndFollowingId = followerAndFollowingId;
        this.dummyUser = dummyUser;
        this.followers = followers;
        this.followings = followings;
    }

    public String getFollowerAndFollowingId() {
        return followerAndFollowingId;
    }

    public void setFollowerAndFollowingId(String followerAndFollowingId) {
        this.followerAndFollowingId = followerAndFollowingId;
    }

    public DummyUser getDummyUser() {
        return dummyUser;
    }

    public void setDummyUser(DummyUser dummyUser) {
        this.dummyUser = dummyUser;
    }

    public List<DummyUser> getFollowers() {
        return followers;
    }

    public void setFollowers(List<DummyUser> followers) {
        this.followers = followers;
    }

    public List<DummyUser> getFollowings() {
        return followings;
    }

    public void setFollowings(List<DummyUser> followings) {
        this.followings = followings;
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
}
