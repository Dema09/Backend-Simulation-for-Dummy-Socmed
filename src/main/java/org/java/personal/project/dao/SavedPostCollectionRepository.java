package org.java.personal.project.dao;

import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.SavedPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedPostCollectionRepository extends MongoRepository<SavedPost, String> {
    List<SavedPost> findAllByDummyUser(DummyUser dummyUser);
}
