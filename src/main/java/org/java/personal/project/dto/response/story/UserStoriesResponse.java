package org.java.personal.project.dto.response.story;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStoriesResponse {
    List<HeadStoryResponse> storyResponses;
}
