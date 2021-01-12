package org.java.personal.project.dto.response;

import java.util.List;

public class MentionUserHeadResponse {
    private List<MentionUserResponse> mentionedUsers;

    public List<MentionUserResponse> getMentionedUsers() {
        return mentionedUsers;
    }

    public void setMentionedUsers(List<MentionUserResponse> mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
    }
}
