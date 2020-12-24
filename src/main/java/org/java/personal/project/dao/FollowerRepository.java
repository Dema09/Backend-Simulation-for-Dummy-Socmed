package org.java.personal.project.dao;

import org.java.personal.project.domain.Follower;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FollowerRepository extends MongoRepository<Follower, String> {
}
