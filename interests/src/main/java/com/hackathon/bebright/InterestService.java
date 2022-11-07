package com.hackathon.bebright;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hackathon.bebright.clients.interests.Interest;
import com.hackathon.bebright.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    public List<Interest> getUsersInterests(String bearerToken) {
        return interestRepository.findByUsername(getUsername(bearerToken));
    }

    public List<Interest> getDifferentUsersByInterest(String interest) {
        return interestRepository.findByInterestType(interest);
    }

    public Interest addAnInterestForUser(String bearerToken, String interestType) {
        // no need to convert to lower caps and remove spaces to save in DB as interest options will be hard-coded in
        // front end (they will all have a specified format from the front end)
        String username = getUsername(bearerToken);

        if (interestRepository.findByUsernameAndInterestType(username, interestType) != null) {
            throw new AppException("This interest type has already been assigned to this user", HttpStatus.BAD_REQUEST);
        }

        Interest interestToAdd = new Interest(username, interestType);

        log.info("Registering interest: {} for user with username: {}", interestType, getUsername(bearerToken));
        return interestRepository.insert(interestToAdd);
    }

    public List<Interest> addInterestListForUser(String bearerToken, List<String> interestTypes) {
        String username = getUsername(bearerToken);
        List<Interest> interestListToAdd = new ArrayList<>();
        interestTypes.forEach(stringInterest -> interestListToAdd.add(new Interest(username, stringInterest)));
        return interestRepository.insert(interestListToAdd);
    }

    public void deleteAnInterestForUser(String bearerToken, String interestType) {
        log.info("Deleting interest of type: {} for user with username: {}", interestType, getUsername(bearerToken));
        interestRepository.deleteByUsernameAndInterestType(getUsername(bearerToken), interestType);
    }

    private String getUsername(String bearerToken) {
        String accessToken = bearerToken.split(" ")[1];
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);
        String username = decodedJWT.getSubject();
        return username;
    }
}
