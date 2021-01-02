package org.java.personal.project.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

public class FollowerAndFollowing {
    @Id
    private String followerAndFollowingId;

    private DummyUser currentUser;

    private List<DummyUser> followers;

    private List<DummyUser> followings;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public FollowerAndFollowing() {
    }

    public FollowerAndFollowing(String followerAndFollowingId, DummyUser currentUser, List<DummyUser> followers, List<DummyUser> followings) {
        this.followerAndFollowingId = followerAndFollowingId;
        this.currentUser = currentUser;
        this.followers = followers;
        this.followings = followings;
    }

    public String getFollowerAndFollowingId() {
        return followerAndFollowingId;
    }

    public void setFollowerAndFollowingId(String followerAndFollowingId) {
        this.followerAndFollowingId = followerAndFollowingId;
    }

    public DummyUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(DummyUser currentUser) {
        this.currentUser = currentUser;
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
