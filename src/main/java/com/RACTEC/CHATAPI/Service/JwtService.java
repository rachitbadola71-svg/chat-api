package com.RACTEC.CHATAPI.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {



    private static final long TOKEN_VALIDITY_TIME_MS = 60*60*1000;
    private final SecretKey key;

    public JwtService(@Value("${JWT_SECRET}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)) ;
    }


    // JOB 1 CREATE THE TOKEN WITH INFO AND STAMP
    public String createToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + TOKEN_VALIDITY_TIME_MS))
                .signWith(key)
                .compact();
    }

    // JOB 2 CHECK THE STAMP AND THE USERID
    public Long getUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload() ;

        return Long.valueOf(claims.getSubject());
    }
}

