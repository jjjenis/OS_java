package org.example.gatewayapi.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.gatewayapi.entities.UserCredentials;
import org.example.gatewayapi.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        logger.debug("Received login request: {}", credentials);
        String login = credentials.get("login");
        String password = credentials.get("password");

        if (login == null || password == null) {
            logger.warn("Missing login or password");
            return ResponseEntity.badRequest().body(Map.of("error", "Missing login or password"));
        }

        try {
            UserCredentials user = authService.authenticate(login, password);
            if (user != null) {
                String token = Jwts.builder()
                        .setSubject(login)
                        .claim("role", user.getRole())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                        .signWith(secretKey)
                        .compact();
                logger.debug("Generated token for {}: {}", login, token);
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            }
            logger.warn("Authentication failed for login: {}", login);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            logger.error("Error during login: ", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
}