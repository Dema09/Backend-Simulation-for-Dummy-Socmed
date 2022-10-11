package org.java.personal.project.repository.repoclass;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.java.personal.project.domain.StoryLatest;
import org.java.personal.project.repository.StoryLatestRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StoryLatestRepositoryImpl implements StoryLatestRepository {

    private String key;
    private RedisTemplate redisTemplate;
    private HashOperations hashOperations;
    private ObjectMapper objectMapper;



    @Override
    public List<StoryLatest> findAllByUserId(String userId) {
        return null;
    }

    @Override
    public <S extends StoryLatest> S save(S s) {
        return null;
    }

    @Override
    public <S extends StoryLatest> Iterable<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public StoryLatest findOne(String s) {
        return null;
    }

    @Override
    public boolean exists(String s) {
        return false;
    }

    @Override
    public Iterable<StoryLatest> findAll() {
        return null;
    }

    @Override
    public Iterable<StoryLatest> findAll(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void delete(StoryLatest storyLatest) {

    }

    @Override
    public void delete(Iterable<? extends StoryLatest> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
