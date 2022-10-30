package com.hackathon.bebright.posts;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    @Query(value = "{'username': ?0}", sort = "{'createdAt': -1}")
    List<Post> findByUsername(String username);

    @Query(value = "{'username': {'$in': ?0}}", sort = "{'createdAt': -1}")
    List<Post> findByUsername(List<String> usernameList);

}
