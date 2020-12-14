package com.epam.esm.security.util;

import com.epam.esm.exception.JwtFormatException;
import com.epam.esm.security.JwtConfig;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;
    private static final String CLAIM_AUTHORITIES = "Authorities";
    private static final Logger LOG = LogManager.getLogger();

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .claim(CLAIM_AUTHORITIES, userDetails.getAuthorities())
                .setIssuedAt(Date.valueOf(LocalDate.now()))
                .setExpiration(Date.valueOf(LocalDate.now().plusDays(jwtConfig.getDaysBeforeExpiration())))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getKeyAsByteArray())
                .compact();
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtConfig.getKeyAsByteArray()).parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtFormatException();
        }

    }

    private boolean tokenExpired(String token) {
        LOG.debug(extractClaims(token)
                .getExpiration());
        return extractClaims(token)
                .getExpiration()
                .before(Date.valueOf(LocalDate.now()));
    }


    public String extractLogin(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean tokenIsValid(String token, UserDetails userDetails) {
        String login = extractLogin(token);
        return (userDetails.getUsername().equals(login) && !tokenExpired(token));
    }
}
