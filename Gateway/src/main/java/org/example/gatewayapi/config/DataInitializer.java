package org.example.gatewayapi.config;

import org.example.gatewayapi.entities.UserCredentials;
import org.example.gatewayapi.repositories.UserCredentialsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserCredentialsRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.findByLogin("admin") == null) {
                UserCredentials admin = new UserCredentials();
                admin.setLogin("admin");
                admin.setPassword(passwordEncoder.encode("adminpass"));
                admin.setRole("ADMIN");
                repository.save(admin);
            }
        };
    }
}