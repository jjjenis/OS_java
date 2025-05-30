package org.example.Mappers;

import org.example.DTOs.TransactionDTO;
import org.example.Entities.Transaction;

public class TransactionMapper {

    public static TransactionDTO toDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setDescription(transaction.getDescription());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setAccountOwnerLogin(transaction.getAccount().getOwnerLogin());
        return transactionDTO;
    }
}