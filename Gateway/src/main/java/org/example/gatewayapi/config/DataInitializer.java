package org.example.gatewayapi.config;

import jakarta.transaction.Transactional;
import org.example.gatewayapi.entities.UserCredentials;
import org.example.gatewayapi.repositories.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserCredentialsRepository userCredentialsRepository, PasswordEncoder passwordEncoder) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Checking for admin user...");
        if (userCredentialsRepository.findByLogin("admin").isEmpty()) {
            UserCredentials admin = new UserCredentials();
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setRole("ADMIN");
            userCredentialsRepository.saveAndFlush(admin); // Ensure data is written to DB
            System.out.println("Admin user created: " + admin.getLogin());
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}