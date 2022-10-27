package com.hackathon.bebright;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hackathon.bebright.clients.interests.Interest;
import com.hackathon.bebright.clients.interests.InterestClient;
import com.hackathon.bebright.exceptions.AppException;
import com.hackathon.bebright.exceptions.InvalidJwtTokenException;
import com.hackathon.bebright.exceptions.InvalidRequestException;
import com.hackathon.bebright.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InterestClient interestClient;

    public User registerNewUser(User user) {
        //TODO: add checking logic to make sure user fields comply with rules otherwise throw exceptions


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User insertedUser = userRepository.insert(user);
        return insertedUser;
    }

    public User validateToken(String accessToken) throws InvalidJwtTokenException {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(accessToken);

            String username = decodedJWT.getSubject();
            return userRepository.findByUsername(username);

        } catch (Exception exception) {
            log.error("Error logging in: {}", exception.getMessage());
            throw new InvalidJwtTokenException("JWT is invalid");
        }

    }

    public List<User> getUsersByOffice(String office) {
        //TODO: Checks for office e.g. what happens if users not found
        //TODO: add normalising logic e.g. convert input to lowercase and take space out to compare to DB documents
        return userRepository.findByOfficesContaining(office);
    }

    public List<User> getUsersByOfficeAndTeam(String office, String team) {
        List<User> usersFromSameOffice = userRepository.findByOfficesContaining(office);

        if (usersFromSameOffice.isEmpty()) {
            throw new InvalidRequestException("There are no users from this office");
        }

        List<String> userIdFromSameOffice = usersFromSameOffice.stream().map(user -> user.getUserId()).collect(Collectors.toList());
        List<User> usersFromSameOfficeAndTeam = userRepository.findByUserIdAndTeamsContaining(userIdFromSameOffice, team);

        if (usersFromSameOfficeAndTeam.isEmpty()) {
            throw new InvalidRequestException("There are no users from this office and team");
        }

        //TODO: add normalising logic e.g. convert input to lowercase and take space out to compare to DB documents
        return userRepository.findByOfficesAndTeamsContaining(office, team);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException("Username not valid", HttpStatus.NOT_FOUND);
        } else return user;
    }

    public List<User> getUsersByOfficeAndInterest(String bearerToken, String interestType) {
        List<Interest> interestList = interestClient.getDifferentUsersByInterest(interestType);

        List<String> usernameWithSameInterestList = interestList.stream().map(interest -> interest.getUsername()).collect(Collectors.toList());

        Collection<String> offices = getUserByUsername(getUsername(bearerToken)).getOffices();
        List<User> usersInSameOffice = userRepository.findByOfficesContaining(offices);

        List<String> usernamesInSameOffice = usersInSameOffice.stream().map(user -> user.getUsername()).collect(Collectors.toList());

        List<String> commonUsernames = usernameWithSameInterestList.stream()
                .filter(usernamesInSameOffice::contains)
                .collect(Collectors.toList());

        List<User> resultingUsers = new ArrayList<>();
        commonUsernames.forEach(username -> resultingUsers.add(userRepository.findByUsername(username)));

        return resultingUsers;
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
