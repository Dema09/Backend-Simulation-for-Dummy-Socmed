package org.java.personal.project.dao;

import org.java.personal.project.domain.StoryLatest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoryLatestRepository extends CrudRepository<StoryLatest, String> {
    StoryLatest findStoryLatestByUserId(String userId);
}
