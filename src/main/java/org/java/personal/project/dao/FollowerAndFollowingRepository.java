package org.java.personal.project.dao;

import org.java.personal.project.domain.DummyUser;
import org.java.personal.project.domain.FollowerAndFollowing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FollowerAndFollowingRepository extends MongoRepository<FollowerAndFollowing, String> {

    FollowerAndFollowing findFollowerAndFollowingByDummyUser(DummyUser dummyUser);
}
