package org.java.personal.project.domain;

import org.springframework.data.annotation.Id;

public class Following {
    @Id
    private String followingId;

    private String userId;

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
