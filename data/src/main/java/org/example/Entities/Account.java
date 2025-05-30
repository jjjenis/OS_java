package org.example.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    public String getPrefixedId() {
        return "ACC" + id;
    }

    public Long getAccountId() {
        return id;
    }

    public void setAccountId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private User user;

    private Long userId;

    private String ownerLogin;

    private double balance;

    @OneToMany(mappedBy = "account",
            cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.EAGER)
    private final List<Transaction> transactionHistory = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(String description) {
        Transaction transaction = new Transaction(description);
        transaction.setAccount(this);
        transactionHistory.add(transaction);
    }
}