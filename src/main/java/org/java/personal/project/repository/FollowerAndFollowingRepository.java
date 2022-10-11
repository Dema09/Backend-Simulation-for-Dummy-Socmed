package org.java.personal.project.repository;

import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.FollowerAndFollowing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowerAndFollowingRepository extends MongoRepository<FollowerAndFollowing, String> {

    FollowerAndFollowing findFollowerAndFollowingByDummyUser(DummyUser dummyUser);
}
