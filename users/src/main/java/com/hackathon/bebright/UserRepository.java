package com.hackathon.bebright;

import com.hackathon.bebright.clients.users.User;
import com.hackathon.bebright.models.Username;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    List<User> findByOfficesContaining(Collection<String> office);
    List<User> findByOfficesContaining(String office);
    List<User> findByOfficesAndTeamsContaining(String office, String team);
    List<User> findByUserIdAndTeamsContaining(List<String> userIdList, String team);

    @Query(value = "{'offices': ?0}", fields = "{'username': 1, '_id': 0}")
    List<Username> findUsernameByOffice(String office);

    @Query(value = "{'offices': {$in: ?0}}", fields = "{'username': 1, '_id': 0}")
    List<Username> findUsernamesByMultipleOffices(Collection<String> offices);

    @Query(value = "{$and: [{'offices': ?0}, {'teams': ?1}]}", fields = "{'username': 1, '_id': 0}")
    List<Username> findUsernameByOfficeAndTeam(String office, String team);
}
