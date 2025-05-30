package org.example.DTOs;

import lombok.Data;

@Data
public class AccountDTO {
    private Long id;
    private String ownerLogin;
    private double balance;
}