package com.hackathon.bebright;

import com.hackathon.bebright.clients.interests.Interest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/interests")
    public ResponseEntity<List<Interest>> getUsersInterests(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        log.info("Getting a user's interests");
        return ResponseEntity.ok(interestService.getUsersInterests(bearerToken));
    }

    @GetMapping("/interests/{interestType}")
    public List<Interest> getDifferentUsersByInterest(@PathVariable("interestType") String interestType) {
        log.info("Getting all the different users with interestType: {}", interestType);
        return interestService.getDifferentUsersByInterest(interestType);
    }

    @PostMapping("interests/{interestType}")
    public ResponseEntity<Interest> addAnInterestForUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                         @PathVariable("interestType") String interestType) {
        return ResponseEntity.ok(interestService.addAnInterestForUser(bearerToken, interestType));
    }

    @PostMapping("interests")
    public ResponseEntity<List<Interest>> addMultipleInterestsForUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                         @RequestBody List<String> interestTypes) {
        return ResponseEntity.ok(interestService.addInterestListForUser(bearerToken, interestTypes));
    }

    @DeleteMapping("interests/{interestType}")
    public void deleteAnInterestForUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                        @PathVariable("interestType") String interestType) {
        interestService.deleteAnInterestForUser(bearerToken, interestType);
    }
}
