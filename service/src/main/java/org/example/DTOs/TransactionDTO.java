package org.example.DTOs;

import lombok.Data;

@Data
public class TransactionDTO {
    private Long id;
    private String description;
    private double amount;
    private String accountOwnerLogin;
}