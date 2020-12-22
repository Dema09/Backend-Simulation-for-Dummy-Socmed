package org.java.personal.project.dao;

import org.java.personal.project.domain.DummyUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<DummyUser, String> {

    DummyUser findDummyUserByUsername(String username);

}
