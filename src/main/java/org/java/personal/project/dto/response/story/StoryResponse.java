package org.java.personal.project.dto.response.story;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoryResponse implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;

    private String storyId;
    private String storyFileBase64;
    private String timeDiffToString;
    private List<String> mentionedUsers;
}
