package com.hackathon.bebright.clients.users;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
//import reactivefeign.spring.config.ReactiveFeignClient;
//import reactor.core.publisher.Mono;

@FeignClient("users")
public interface UserClient {

//    @GetMapping("users/authenticate/{username}/{password")
//    public User checkUserCredentials(@PathVariable("username") String username, @PathVariable("password") String password);

    @GetMapping("/users/getUsersByOffice/{office}")
    ResponseEntity<List<User>> getUsersByOffice(@PathVariable("office") String office);
}

//@ReactiveFeignClient(value = "users", url = "http://localhost:8082")
//public interface UserClient {
//    @GetMapping("users/authenticate/{username}/{password")
//    User checkUserCredentials(@PathVariable("username") String username, @PathVariable("password") String password);
//}
