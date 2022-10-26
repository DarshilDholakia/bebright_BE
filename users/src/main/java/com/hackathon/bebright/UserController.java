package com.hackathon.bebright;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.bebright.exceptions.InvalidJwtTokenException;
import com.hackathon.bebright.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<User> registerNewUser(@RequestBody User user) {
        log.info("Registering a new user. The user details are {}", user);
        return ResponseEntity.ok(userService.registerNewUser(user));
    }
    @PostMapping("/validateToken")
    public User validateToken(@RequestParam String token) throws InvalidJwtTokenException {
        log.info("Trying to validate token {}", token);
        return userService.validateToken(token);
    }

    @GetMapping("/users/getUsersByOffice/{office}")
    public List<User> getUsersByOffice(@PathVariable("office") String office) {
        log.info("Fetching data for users from {} office", office);
        return userService.getUsersByOffice(office);
    }

    @GetMapping("/users/getUsersByOfficeAndTeam/{office}/{team}")
    public ResponseEntity<List<User>> getUsersByOfficeAndTeam(@PathVariable("office") String office, @PathVariable("team") String team) {
        log.info("Fetching data for users from {} office and {} team", office, team);
        return ResponseEntity.ok(userService.getUsersByOfficeAndTeam(office, team));
    }

    @GetMapping("users/getUsersByOfficeAndInterest/{interestType}")
    public ResponseEntity<List<User>> getUsersByOfficeAndInterest(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                                  @PathVariable("interestType") String interestType) {
        log.info("Fetching users from user's offices with interestType: {}", interestType);
        return ResponseEntity.ok(userService.getUsersByOfficeAndInterest(bearerToken, interestType));
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                User user = userService.getUserByUsername(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
