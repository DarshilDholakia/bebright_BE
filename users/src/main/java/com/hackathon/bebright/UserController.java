package com.hackathon.bebright;

import com.hackathon.bebright.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.channels.ReadPendingException;

@RestController
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // This Login method generates a JWT token
    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody String userID) {
        if (userService.checkIfUserExists(userID)) {
            //Once we have ensured a user exists with this ID only then can a JWT token be generated.
            String token = jwtUtil.generateToken(userID);
            return new ResponseEntity<String>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/user/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

}
