package com.hackathon.bebright.posts;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    @Query(value = "{'username': ?0}", sort = "{'createdAt': -1}")
    List<Post> findByUsername(String username);

    @Query(value = "{'username': {'$in': ?0}}", sort = "{'createdAt': -1}")
    List<Post> findByUsername(List<String> usernameList);

    @Query(value = "{$and: [" +
            "{'username': ?0}," +
            "{$and: [" +
                    "{'createdAt': {'$gte': ?1}}," +
                    "{'createdAt': {'$lte': ?2}}" +
                    "]" +
            "}" +
            "]}")
    Optional<Post> findByUsernameAndDate(String username, LocalDateTime startOfDay, LocalDateTime endOfDay);

}
