package org.example.gatewayapi.services;

import org.example.gatewayapi.requests_g.CreateUserRequest;
import org.example.gatewayapi.requests_g.DepositRequest;
import org.example.gatewayapi.requests_g.WithdrawRequest;
import org.example.gatewayapi.requests_g.TransferRequest;
import org.example.gatewayapi.requests_g.AddFriendRequest;
import org.example.gatewayapi.requests_g.RemoveFriendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.example.gatewayapi.repositories.UserCredentialsRepository;
import org.example.gatewayapi.entities.UserCredentials;
import org.example.gatewayapi.entities.enums.HairCOLOR;

import java.util.Collections;
import java.util.Optional;

@Service
public class AdminService {
    private final RestTemplate restTemplate;

    @Value("${main.service.url}")
    private String mainServiceUrl;

    @Autowired
    public AdminService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createUser(CreateUserRequest request, String token) {
        if (request.getHairColor() == null) {
            throw new IllegalArgumentException("Hair color must not be null");
        }
        String url = mainServiceUrl + "/api/users";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<CreateUserRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public String createAdmin(CreateUserRequest request, String token) {
        if (request.getHairColor() == null) {
            throw new IllegalArgumentException("Hair color must not be null");
        }
        String url = mainServiceUrl + "/api/users";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<CreateUserRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}
