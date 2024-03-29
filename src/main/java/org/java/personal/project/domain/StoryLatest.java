package org.java.personal.project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "UserStory", timeToLive = 86400)
public class StoryLatest {
    @Id
    @Indexed
    private String storyId;
    @Indexed
    private String userId;
    @Indexed
    private String username;
    @CreatedDate
    private Date createdDate;
    private String storyFileName;
    private String storyLocation;
    private List<DummyUser> mentionedUsername;
    private boolean isCloseFriendMode;
}
