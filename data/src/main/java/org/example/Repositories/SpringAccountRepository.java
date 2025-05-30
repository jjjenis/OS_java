package org.example.Repositories;

import org.example.Entities.Account;
import org.example.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringAccountRepository extends JpaRepository<Account, String> {

    default List<Transaction> findTransactionsByAccountId(String Id) {
        return null;
    }

    List<Account> findByOwnerLogin(String ownerLogin);

    List<Account> findAccountsByUserId(Long userId);
}