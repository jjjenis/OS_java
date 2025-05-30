package org.example.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "account_transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_Id")
    private Account account;
    public Transaction() {
    }

    public Transaction(String description) {
        this.description = description;
    }

    public Long getId() {
        return transactionId;
    }

    public void setId(Long id) {
        this.transactionId = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getAmount() {
        return account.getBalance();
    }

    public void setAmount(double amount) {
        account.setBalance(amount);
    }
}