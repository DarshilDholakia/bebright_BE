package com.hackathon.bebright.security;

import com.hackathon.bebright.exception.JwtTokenMalformedException;
import com.hackathon.bebright.exception.JwtTokenMissingException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.util.Date;

// This class is responsible for validating the token if it's present. It does this by parsing the token to see if it is
// valid.
@Component
public class JwtTokenUtil {
    private final JwtConfig config;

    public JwtTokenUtil(JwtConfig config) {
        this.config = config;
    }

    public String generateToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + config.getValidity() * 1000 * 60;
        Date exp = new Date(expMillis);
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, config.getSecret()).compact();
    }

    public void validateToken(final String header) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
//            String[] parts = header.split(" ");
//            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
//                throw new JwtTokenIncorrectStructureException("Incorrect Authentication Structure");
//            }
            Jwts.parser().setSigningKey(config.getSecret()).parseClaimsJws(header);
        } catch (SignatureException ex) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }
    }
}
