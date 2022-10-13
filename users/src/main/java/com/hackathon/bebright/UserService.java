package com.hackathon.bebright;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User checkIfUserExistsById(String userID) {
        return userRepository.findById(userID).get();
    }

    public User checkUserCredentialsAreCorrect(String username, String password) {
        User user = userRepository.findByUsername(username);
        //TODO: add logic to throw exceptions if user with username does not exist
        if (user.getPassword().equals(password) && user.getUsername().equals(username)) return user;
        //TODO: throw custom exception here?
        else throw new IllegalStateException("Invalid user credentials");
    }


    public User registerUser(User user) {
        //TODO: add checks for user fields
        return userRepository.insert(user);
    }
}
