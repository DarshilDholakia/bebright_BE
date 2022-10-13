package com.hackathon.bebright.clients.users;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("users")
public interface UserClient {

    @GetMapping("users/authenticate/{username}/{password")
    public User checkUserCredentials(@PathVariable("username") String username, @PathVariable("password") String password);
}
