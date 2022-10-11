package org.java.personal.project.repository;

import org.java.personal.project.domain.StoryLatest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryLatestRepository extends CrudRepository<StoryLatest, String> {

    List<StoryLatest> findAllByUserId(String userId);
}
