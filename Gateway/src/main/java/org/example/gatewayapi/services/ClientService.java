package org.example.gatewayapi.services;

import jakarta.transaction.Transactional;
import org.example.gatewayapi.entities.UserCredentials;
import org.example.gatewayapi.repositories.UserCredentialsRepository;
import org.example.gatewayapi.requests_g.*;
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
        Optional<UserCredentials> userOpt = userCredentialsRepository.findByLogin(username);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        UserCredentials user = userOpt.get();
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    public String addFriend(AddFriendRequest request, String token) {
        String url = mainServiceUrl + "/api/friends/add";
        return postWithAuth(url, request, token);
    }

    public String removeFriend(RemoveFriendRequest request, String token) {
        String url = mainServiceUrl + "/api/friends/remove";
        return postWithAuth(url, request, token);
    }

    public String deposit(DepositRequest request, String token) {
        String url = mainServiceUrl + "/api/accounts/deposit";
        return postWithAuth(url, request, token);
    }

    public String withdraw(WithdrawRequest request, String token) {
        String url = mainServiceUrl + "/api/accounts/withdraw";
        return postWithAuth(url, request, token);
    }

    public String transfer(TransferRequest request, String token) {
        String url = mainServiceUrl + "/api/accounts/transfer";
        return postWithAuth(url, request, token);
    }

    private <T> String postWithAuth(String url, T body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<T> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}
