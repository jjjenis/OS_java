package org.example.gatewayapi.controllers;

import org.example.gatewayapi.requests_g.CreateUserRequest;
import org.example.gatewayapi.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(adminService.createUser(request, token.replace("Bearer ", "")));
    }

    @PostMapping("/admins")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody CreateUserRequest request, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(adminService.createAdmin(request, token.replace("Bearer ", "")));
    }
}