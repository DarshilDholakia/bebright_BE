package com.hackathon.bebright;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean checkIfUserExists(String userID) {
        return userRepository.findById(userID).isPresent();
    }
    public User registerUser(User user) {
        //TODO: add checks for user fields
        return userRepository.insert(user);
    }
}
