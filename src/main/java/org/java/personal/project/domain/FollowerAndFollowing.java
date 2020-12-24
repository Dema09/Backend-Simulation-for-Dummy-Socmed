package org.java.personal.project.domain;

import org.springframework.data.annotation.Id;

import java.util.List;

public class FollowerAndFollowing {
    @Id
    private String followerAndFollowingId;

    private DummyUser currentUser;

    private List<Follower> followers;

    private List<Following> followings;

    public FollowerAndFollowing() {
    }

    public FollowerAndFollowing(String followerAndFollowingId, DummyUser currentUser, List<Follower> followers, List<Following> followings) {
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

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public List<Following> getFollowings() {
        return followings;
    }

    public void setFollowings(List<Following> followings) {
        this.followings = followings;
    }
}
