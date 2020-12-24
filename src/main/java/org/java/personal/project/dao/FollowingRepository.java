package org.java.personal.project.dao;

import org.java.personal.project.domain.Following;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FollowingRepository extends MongoRepository<Following, String> {
}
