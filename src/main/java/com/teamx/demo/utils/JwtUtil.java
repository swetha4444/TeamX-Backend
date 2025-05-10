package com.teamx.demo.utils;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("your_secret_key_which_is_at_least_32_bytes!".getBytes());

    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}