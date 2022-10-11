package org.java.personal.project.repository;

import org.java.personal.project.domain.PostOrStoryLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostOrStoryLocationRepository extends MongoRepository<PostOrStoryLocation, String> {

    PostOrStoryLocation findPostOrStoryLocationByLocationName(String locationName);

}
