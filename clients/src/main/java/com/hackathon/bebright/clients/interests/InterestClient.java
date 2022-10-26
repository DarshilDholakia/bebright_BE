package com.hackathon.bebright.clients.interests;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("interests")
public interface InterestClient {

    @GetMapping("/interests/{interestType}")
    List<Interest> getDifferentUsersByInterest(@PathVariable("interestType") String interestType);
}
