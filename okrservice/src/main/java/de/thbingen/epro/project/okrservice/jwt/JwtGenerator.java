package de.thbingen.epro.project.okrservice.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import de.thbingen.epro.project.okrservice.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerator {

    private static final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SecurityConstants.JWT_SECRET));

    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                            .subject(email)
                            .issuedAt(currentDate)
                            .expiration(expireDate)
                            .signWith(key)
                            .compact();

        return token;
    }


    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser().verifyWith(key)
                            .build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key)
                .build().parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect!", ex.fillInStackTrace());
        }
    }

}
