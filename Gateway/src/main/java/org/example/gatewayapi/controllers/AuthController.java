package org.example.gatewayapi.controllers;

import io.jsonwebtoken.Jwts;
import org.example.gatewayapi.entities.UserCredentials;
import org.example.gatewayapi.requests_g.CreateUserRequest;
import org.example.gatewayapi.repositories.UserCredentialsRepository;
import org.example.gatewayapi.services.AuthService;
import org.example.gatewayapi.util.PasswordEncoderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.example.gatewayapi.config.SecurityConfig.SECRET_KEY;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

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
                        .signWith(SECRET_KEY)
                        .compact();

                logger.debug("Generated token for {}: {}", login, token);
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Authentication failed for login: {}", login);
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }
        } catch (Exception e) {
            logger.error("Error during login: ", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody CreateUserRequest request) {
        if (userCredentialsRepository.findByLogin(request.getLogin()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        UserCredentials user = new UserCredentials();
        user.setLogin(request.getLogin());
        user.setPassword(PasswordEncoderUtil.encode(request.getPassword()));
        user.setRole(request.getRole());

        userCredentialsRepository.save(user);
        return ResponseEntity.ok("User created");
    }

    // Добавим возможность создавать администратора напрямую через Gateway без RestTemplate
    @PostMapping("/admin/create")
    public ResponseEntity<String> createAdmin(@RequestBody CreateUserRequest request) {
        if (userCredentialsRepository.findByLogin(request.getLogin()).isPresent()) {
            return ResponseEntity.badRequest().body("Admin already exists");
        }

        UserCredentials admin = new UserCredentials();
        admin.setLogin(request.getLogin());
        admin.setPassword(PasswordEncoderUtil.encode(request.getPassword()));
        admin.setRole("ADMIN");

        userCredentialsRepository.save(admin);
        return ResponseEntity.ok("Admin created");
    }
}
