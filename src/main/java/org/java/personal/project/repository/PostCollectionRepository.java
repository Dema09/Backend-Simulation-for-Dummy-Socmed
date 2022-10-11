package org.java.personal.project.repository;

import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.PostCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCollectionRepository extends MongoRepository<PostCollection, String> {
    List<PostCollection> findAllByDummyUser(DummyUser dummyUser);

    PostCollection findPostCollectionByPostCollectionIdAndDummyUser(String postCollectionId, DummyUser dummyUser);

    PostCollection findPostCollectionByPostCollectionName(String postCollectionName);
}
