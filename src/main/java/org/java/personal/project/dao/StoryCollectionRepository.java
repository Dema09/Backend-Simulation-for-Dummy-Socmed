package org.java.personal.project.dao;

import org.java.personal.project.domain.StoryCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryCollectionRepository extends MongoRepository<StoryCollection, String> {



}