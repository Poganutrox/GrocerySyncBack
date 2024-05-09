package com.miguelangel.supermarketDataCollector.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.miguelangel.supermarketDataCollector.configuration.security.SecurityConstants.SECRET_KEY;
import static com.miguelangel.supermarketDataCollector.configuration.security.SecurityConstants.TOKEN_EXPIRATION;

@Component
public class TokenGenerator {
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt( new Date())
                .setExpiration(expireDate)
                .signWith(SECRET_KEY,SignatureAlgorithm.HS512)
                .compact();
    }
    public String getEmailFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("Token was expired or incorrect",ex.fillInStackTrace());
        }
    }

}