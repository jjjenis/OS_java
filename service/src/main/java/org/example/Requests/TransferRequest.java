package org.example.Requests;

import lombok.Data;

@Data
public class TransferRequest {
    private String fromAccountId;
    private String toAccountId;
    private double amount;
}