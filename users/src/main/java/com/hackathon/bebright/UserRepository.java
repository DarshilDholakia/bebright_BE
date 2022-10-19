package com.hackathon.bebright;

import com.hackathon.bebright.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    List<User> findByOffice(String office);

    List<User> findByOfficeAndTeam(String office, String team);
}
