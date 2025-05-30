package org.example.gatewayapi.services;

import org.example.gatewayapi.entities.UserCredentials;
import org.example.gatewayapi.repositories.UserCredentialsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserCredentialsRepository userCredentialsRepository, PasswordEncoder passwordEncoder) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserCredentials authenticate(String login, String password) {
        logger.debug("Authenticating login: {}", login);
        UserCredentials user = userCredentialsRepository.findByLogin(login);
        if (user == null) {
            logger.debug("No user found for login: {}", login);
            return null;
        }
        logger.debug("Found user: {}, password match: {}", user.getLogin(), passwordEncoder.matches(password, user.getPassword()));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}