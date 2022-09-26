package org.java.personal.project.dao;

import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.Story;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface StoryRepository extends MongoRepository<Story, String> {
    List<Story> findStoriesByCurrentUserStory(DummyUser dummyUser);

    Story findStoryByStoryIdAndCurrentUserStory(String storyId, DummyUser dummyUser);

}
