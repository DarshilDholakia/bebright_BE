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
        return ResponseEntity.ok(userService.getUsersByOffice(office));
    }

}
