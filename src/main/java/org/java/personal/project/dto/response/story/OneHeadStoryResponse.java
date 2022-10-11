package org.java.personal.project.dto.response.story;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneHeadStoryResponse {
    String username;
    StoryResponse story;

}
