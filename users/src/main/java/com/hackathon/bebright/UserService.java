package com.hackathon.bebright;

import com.hackathon.bebright.exceptions.AppException;
import com.hackathon.bebright.models.CredentialsDto;
import com.hackathon.bebright.models.User;
import com.hackathon.bebright.models.UserDto;
//import com.hackathon.bebright.util.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.CharBuffer;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final UserMapper userMapper;

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public UserDto validateToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        Optional<User> userOptional = Optional.of(userRepository.findByUsername(username));

        if (userOptional.isEmpty()) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
//        return userMapper.toUserDto(user, createToken(user));
        return UserDto.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .token(createToken(user))
                .build();
    }

    public Object signIn(CredentialsDto credentialsDto) {
        Optional<User> userOptional = Optional.of(userRepository.findByUsername(credentialsDto.getUsername()));
        if (userOptional.isEmpty()) return new AppException("User not found", HttpStatus.NOT_FOUND);

        if (credentialsDto.getPassword().equals(userOptional.get().getPassword())) {
//            return userMapper.toUserDto(userOptional.get(), createToken(userOptional.get()));
            return UserDto.builder()
                    .id(userOptional.get().getUserId())
                    .username(userOptional.get().getUsername())
                    .token(createToken(userOptional.get()))
                    .build();
        }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    private String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());

        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hour
        // 1 hour = 3600000
        // 1 min = 60000
        // 10 sec = 10000

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public List<User> getUsersByOffice(String office) {
        //TODO: Checks for office string
        return userRepository.findByOffice(office);
    }
}
