package org.java.personal.project.dto;

import org.java.personal.project.dto.response.MentionUserResponse;

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
