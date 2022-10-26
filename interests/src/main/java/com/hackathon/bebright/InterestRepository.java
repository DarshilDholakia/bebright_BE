package com.hackathon.bebright;

import com.hackathon.bebright.clients.interests.Interest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends MongoRepository<Interest, String> {

    List<Interest> findByUsername(String username);

    List<Interest> findByInterestType(String interest);

    Interest findByUsernameAndInterestType(String username, String interestType);

    void deleteByUsernameAndInterestType(String username, String interestType);
}
