package com.hackathon.bebright;

import com.hackathon.bebright.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.ReadPendingException;

@RestController
@RequestMapping("users")
public class UserController {
    private JwtUtil jwtUtil;
    private UserService userService;

    public UserController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("authenticate/{username}/{password}")
    public User checkUserCredentials(@PathVariable("username") String username, @PathVariable("password") String password) {
        return userService.checkUserCredentialsAreCorrect(username, password);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

}
