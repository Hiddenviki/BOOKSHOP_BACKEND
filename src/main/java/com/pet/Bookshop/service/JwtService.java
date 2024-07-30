package com.pet.Bookshop.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pet.Bookshop.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtService {
    @Value("${spring.application.security.jwtSecret}")
    private String jwtSecret; //сЕкрЕтнаЯ сТроКа
    @Value("${spring.application.security.jwtExpirationMs}") //дата окончания
    private int jwtExpirationMs;

    //генерация токена
    public String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getLogin())
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("login", user.getLogin())
                .claim("role", user.getRole())
                .setIssuer("vveselkova")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Генерация ключа с использованием jwtSecret
    private Key generateKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String validateTokenAndRetrieveClaim(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("vveselkova")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("login").asString();
        } catch (JWTVerificationException exception) {
            log.error("Invalid JWT token: "+ exception);

            return "";
        }
    }
}