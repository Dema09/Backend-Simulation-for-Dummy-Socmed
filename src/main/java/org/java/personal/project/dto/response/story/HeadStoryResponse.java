package org.java.personal.project.dto.response.story;

import java.util.List;

public class HeadStoryResponse {
    private List<StoryResponse> stories;

    public List<StoryResponse> getStories() {
        return stories;
    }

    public void setStories(List<StoryResponse> stories) {
        this.stories = stories;
    }
}
