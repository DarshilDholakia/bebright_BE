package com.hackathon.bebright;

import com.hackathon.bebright.models.CredentialsDto;
import com.hackathon.bebright.models.User;
import com.hackathon.bebright.models.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/registerNewUser")
    public ResponseEntity<Object> registerNewUser(@RequestBody User user) {
        log.info("Registering a new user. The user details are {}", user);
        return ResponseEntity.ok(userService.registerNewUser(user));
    }

    @PostMapping("/signIn")
    public ResponseEntity<Object> signIn(@RequestBody CredentialsDto credentialsDto) {
        log.info("Trying to login {}", credentialsDto.getUsername());
        return ResponseEntity.ok(userService.signIn(credentialsDto));
    }

    @PostMapping("/validateToken")
    public ResponseEntity<UserDto> signIn(@RequestParam String token) {
        log.info("Trying to validate token {}", token);
        return ResponseEntity.ok(userService.validateToken(token));
    }

    @GetMapping("/getUsersByOffice/{office}")
    public ResponseEntity<List<User>> getUsersByOffice(@PathVariable("office") String office) {
        log.info("Fetching data for users from {} office", office);
        return ResponseEntity.ok(userService.getUsersByOffice(office));
    }

    @GetMapping("/getUsersByOfficeAndTeam/{office}/{team}")
    public ResponseEntity<List<User>> getUsersByOfficeAndTeam(@PathVariable("office") String office, @PathVariable("team") String team) {
        log.info("Fetching data for users from {} office and {} team", office, team);
        return ResponseEntity.ok(userService.getUsersByOfficeAndTeam(office, team));
    }

}
