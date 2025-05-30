package org.example.gatewayapi.services;

import jakarta.transaction.Transactional;
import org.example.gatewayapi.entities.UserCredentials;
import org.example.gatewayapi.repositories.UserCredentialsRepository;
import org.example.gatewayapi.requests.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService implements UserDetailsService {
    private final UserCredentialsRepository userCredentialsRepository;

    private final RestTemplate restTemplate;
    @Value("${main.service.url}")
    private String mainServiceUrl;
    @Autowired
    public ClientService(UserCredentialsRepository userCredentialsRepository, RestTemplate restTemplate) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.restTemplate = restTemplate;
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user with username: [" + username + "]");
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username is null or empty!");
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }
        Optional<UserCredentials> userOpt = userCredentialsRepository.findByLogin(username);
        System.out.println("Query result for login [" + username + "]: " + userOpt.map(UserCredentials::getLogin).orElse("Not found"));
        if (userOpt.isEmpty()) {
            System.out.println("User not found in database for login: [" + username + "]");
            List<UserCredentials> allUsers = userCredentialsRepository.findAll();
            System.out.println("All users in database: " + allUsers);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        UserCredentials user = userOpt.get();
        System.out.println("Loaded user: " + user.getLogin() + ", Role: " + user.getRole());
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
    }


    public String addFriend(AddFriendRequest request, String token) {
        String url = mainServiceUrl + "/client/friends/add";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<AddFriendRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public String removeFriend(RemoveFriendRequest request, String token) {
        String url = mainServiceUrl + "/client/friends/remove";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<RemoveFriendRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public String deposit(DepositRequest request, String token) {
        String url = mainServiceUrl + "/client/accounts/deposit";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<DepositRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public String withdraw(WithdrawRequest request, String token) {
        String url = mainServiceUrl + "/client/accounts/withdraw";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<WithdrawRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public String transfer(TransferRequest request, String token) {
        String url = mainServiceUrl + "/client/accounts/transfer";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TransferRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}