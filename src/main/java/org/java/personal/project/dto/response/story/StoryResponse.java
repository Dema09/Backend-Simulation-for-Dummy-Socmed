package org.java.personal.project.dto.response.story;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoryResponse {
    private String storyId;
    private String username;
    private String storyFileBase64;
    private String timeDiffToString;
    private List<String> mentionedUsers;
}
