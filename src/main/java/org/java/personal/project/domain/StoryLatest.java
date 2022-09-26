package org.java.personal.project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "UserStory", timeToLive = 86400)
public class StoryLatest {
    private String storyId;
    private String userId;
    private String username;
    private String createdAt;
    private String storyFileName;
    private String storyLocation;
    private List<DummyUser> mentionedUsername;
    private boolean isCloseFriendMode;
}
