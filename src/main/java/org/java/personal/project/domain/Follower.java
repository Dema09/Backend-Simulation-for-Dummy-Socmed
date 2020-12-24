package org.java.personal.project.domain;

import org.springframework.data.annotation.Id;

public class Follower {
    @Id
    private String followerId;

    private DummyUser userId;

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public DummyUser getUserId() {
        return userId;
    }

    public void setUserId(DummyUser userId) {
        this.userId = userId;
    }
}
