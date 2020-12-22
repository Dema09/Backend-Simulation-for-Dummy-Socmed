package org.java.personal.project.dto.response.story;

import java.util.List;

public class StoryResponseAfterInsert {
    private List<String> usernameError;
    private String insertStoryResponse;

    public List<String> getUsernameError() {
        return usernameError;
    }

    public void setUsernameError(List<String> usernameError) {
        this.usernameError = usernameError;
    }

    public String getInsertStoryResponse() {
        return insertStoryResponse;
    }

    public void setInsertStoryResponse(String insertStoryResponse) {
        this.insertStoryResponse = insertStoryResponse;
    }
}
