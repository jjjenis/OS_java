package org.example.gatewayapi.services;

import org.example.gatewayapi.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        String url = mainServiceUrl + "/admin/users";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<CreateUserRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public String createAdmin(CreateUserRequest request, String token) {
        String url = mainServiceUrl + "/admin/admins";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<CreateUserRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}