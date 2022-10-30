package com.hackathon.bebright.clients.users;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Collection;
import java.util.List;
//import reactivefeign.spring.config.ReactiveFeignClient;
//import reactor.core.publisher.Mono;

@FeignClient("users")
public interface UserClient {

//    @GetMapping("users/authenticate/{username}/{password")
//    public User checkUserCredentials(@PathVariable("username") String username, @PathVariable("password") String password);

    @GetMapping("/users/getUsersByOffice/{office}")
    List<User> getUsersByOffice(@PathVariable("office") String office);

    @GetMapping("/users/getUsersByOfficeAndTeam/{office}/{team}")
    List<User> getUsersByOfficeAndTeam(@PathVariable("office") String office, @PathVariable("team") String team);

    @GetMapping("/users/getUserByUsername/{username}")
    public User getUserByUsername(@PathVariable("username") String username);

    @GetMapping("users/getUsernameByOffice/{office}")
    List<String> getUsernamesByOffice(@PathVariable("office") String office);

    @GetMapping("/users/getUsernamesByMultipleOffices/{token}")
    List<String> getUsernamesByMultipleOffices(@PathVariable("token") String bearerToken);

    @GetMapping("users/getUsernameByOfficeAndTeam/{office}/{team}")
    List<String> getUsernamesByOfficeAndTeam(@PathVariable("office") String office, @PathVariable("team") String team);
}

//@ReactiveFeignClient(value = "users", url = "http://localhost:8082")
//public interface UserClient {
//    @GetMapping("users/authenticate/{username}/{password")
//    User checkUserCredentials(@PathVariable("username") String username, @PathVariable("password") String password);
//}
