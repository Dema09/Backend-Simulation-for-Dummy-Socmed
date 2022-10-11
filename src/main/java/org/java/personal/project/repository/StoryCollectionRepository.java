package org.java.personal.project.repository;

import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.StoryCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryCollectionRepository extends MongoRepository<StoryCollection, String> {
    StoryCollection findStoryCollectionByCollectionIdAndDummyUser(String collectionId, DummyUser dummyUser);
}
