package org.java.personal.project.dao;

import org.java.personal.project.domain.DummyUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<DummyUser, String> {

    List<DummyUser> findDummyUsersByUsernameLikeOrderByUsernameAsc(String username);

    DummyUser findDummyUserByUsername(String username);

}
