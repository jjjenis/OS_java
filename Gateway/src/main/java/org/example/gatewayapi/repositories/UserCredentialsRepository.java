package org.example.gatewayapi.repositories;

import org.example.gatewayapi.entities.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByLogin(String login);
}