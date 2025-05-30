package org.example.Mappers;

import org.example.DTOs.AccountDTO;
import org.example.Entities.Account;

public class AccountMapper {

    public static AccountDTO toDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setOwnerLogin(account.getOwnerLogin());
        accountDTO.setBalance(account.getBalance());
        return accountDTO;
    }
}