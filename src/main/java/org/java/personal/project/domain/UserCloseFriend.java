package org.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user_close_friend")
public class UserCloseFriend {

    @Id
    private String closeFriendId;

    @DBRef
    private DummyUser userId;

    @DBRef
    private List<DummyUser> closeFriendUsers;

    public String getCloseFriendId() {
        return closeFriendId;
    }

    public void setCloseFriendId(String closeFriendId) {
        this.closeFriendId = closeFriendId;
    }

    public DummyUser getUserId() {
        return userId;
    }

    public void setUserId(DummyUser userId) {
        this.userId = userId;
    }

    public List<DummyUser> getCloseFriendUsers() {
        return closeFriendUsers;
    }

    public void setCloseFriendUsers(List<DummyUser> closeFriendUsers) {
        this.closeFriendUsers = closeFriendUsers;
    }
}
