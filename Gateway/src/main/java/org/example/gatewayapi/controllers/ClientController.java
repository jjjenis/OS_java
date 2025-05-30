package org.example.gatewayapi.controllers;

import org.example.gatewayapi.requests.*;
import org.example.gatewayapi.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/friends/add")
    public ResponseEntity<?> addFriend(@Valid @RequestBody AddFriendRequest request, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(clientService.addFriend(request, token.replace("Bearer ", "")));
    }

    @PostMapping("/friends/remove")
    public ResponseEntity<?> removeFriend(@Valid @RequestBody RemoveFriendRequest request, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(clientService.removeFriend(request, token.replace("Bearer ", "")));
    }

    @PostMapping("/accounts/deposit")
    public ResponseEntity<?> deposit(@Valid @RequestBody DepositRequest request, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(clientService.deposit(request, token.replace("Bearer ", "")));
    }

    @PostMapping("/accounts/withdraw")
    public ResponseEntity<?> withdraw(@Valid @RequestBody WithdrawRequest request, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(clientService.withdraw(request, token.replace("Bearer ", "")));
    }

    @PostMapping("/accounts/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest request, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(clientService.transfer(request, token.replace("Bearer ", "")));
    }
}