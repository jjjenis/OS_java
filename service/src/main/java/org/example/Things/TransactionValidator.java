package org.example.Things;

public class TransactionValidator {
    public void validateWithdraw(double amount, double balance) throws InsufficientFundsException {
        if (amount <= 0) throw new InsufficientFundsException("Amount must be positive");
        if (amount > balance) throw new InsufficientFundsException("Insufficient funds");
    }

    public void validateDeposit(double amount) throws IllegalArgumentException {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
    }

    public void validateTransfer(double amount, double fromBalance) throws InsufficientFundsException {
        validateWithdraw(amount, fromBalance);
    }
}
