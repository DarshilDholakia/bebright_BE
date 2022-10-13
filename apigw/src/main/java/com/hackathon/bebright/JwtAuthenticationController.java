package com.hackathon.bebright;

import com.hackathon.bebright.clients.users.User;
import com.hackathon.bebright.clients.users.UserClient;
import com.hackathon.bebright.models.AuthenticationStatus;
import com.hackathon.bebright.models.ErrorResponseDto;
import com.hackathon.bebright.models.JwtRequest;
import com.hackathon.bebright.models.JwtResponse;
import com.hackathon.bebright.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class JwtAuthenticationController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private UserClient userClient;

    public JwtAuthenticationController(JwtTokenUtil jwtTokenUtil, UserClient userClient) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userClient = userClient;
    }

    @PostMapping("auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        AuthenticationStatus status = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        if (!status.getIsAuthenticated()) {
            List<String> details = new ArrayList<>();
            details.add(status.getMessage());
            ErrorResponseDto error = new ErrorResponseDto(new Date(), HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", details, "uri");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        final String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private AuthenticationStatus authenticate(String username, String password) {
        if (userClient.checkUserCredentials(username, password) != null) return new AuthenticationStatus(true, "Authentication successful");
        else return new AuthenticationStatus(false, "Invalid Username/Password");
    }
}
