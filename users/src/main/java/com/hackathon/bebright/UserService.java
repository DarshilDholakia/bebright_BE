package com.hackathon.bebright;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hackathon.bebright.exceptions.AppException;
import com.hackathon.bebright.models.CredentialsDto;
import com.hackathon.bebright.models.User;
import com.hackathon.bebright.models.UserDto;
//import com.hackathon.bebright.util.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Object registerNewUser(User user) {
        //TODO: add checking logic to make sure user fields comply with rules otherwise throw exceptions
        User insertedUser = userRepository.insert(user);
        return UserDto.builder()
                .id(insertedUser.getUserId())
                .username(insertedUser.getUsername())
                .token(createToken(insertedUser))
                .build();
    }

    public void validateToken(String accessToken) {
//        String username = Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);
        String username = decodedJWT.getSubject();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

//        Optional<User> userOptional = Optional.of(userRepository.findByUsername(username));

//        if (userOptional.isEmpty()) {
//            throw new AppException("User not found", HttpStatus.NOT_FOUND);
//        }

//        return UserDto.builder()
//                .id(userOptional.get().getUserId())
//                .username(userOptional.get().getUsername())
//                .token(createToken(userOptional.get()))
//                .build();



    }

    public Object login(CredentialsDto credentialsDto) {
        Optional<User> userOptional = Optional.of(userRepository.findByUsername(credentialsDto.getUsername()));
        if (userOptional.isEmpty()) return new AppException("User not found", HttpStatus.NOT_FOUND);

        if (credentialsDto.getPassword().equals(userOptional.get().getPassword())) {
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
        //TODO: Checks for office e.g. what happens if users not found
        //TODO: add normalising logic e.g. convert input to lowercase and take space out to compare to DB documents
        return userRepository.findByOffice(office);
    }

    public List<User> getUsersByOfficeAndTeam(String office, String team) {
        //TODO: Checks for office and team e.g. what happens if users not found
        //TODO: add normalising logic e.g. convert input to lowercase and take space out to compare to DB documents
        return userRepository.findByOfficeAndTeam(office, team);
    }
}
