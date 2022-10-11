package org.java.personal.project.repository;

import org.java.personal.project.domain.Comment;
import org.java.personal.project.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findAllByPost(Post post);
}
