package org.java.personal.project.dao;

import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> getAllByDummyUser(DummyUser dummyUser);

}
