package org.example.Services;

import jakarta.transaction.Transactional;
import org.example.DTOs.AccountDTO;
import org.example.DTOs.TransactionDTO;
import org.example.Mappers.AccountMapper;
import org.example.Mappers.TransactionMapper;
import org.example.Entities.Account;
import org.example.Entities.Transaction;
import org.example.Entities.User;
import org.example.Repositories.SpringAccountRepository;
import org.example.Things.AccountNotFoundException;
import org.example.Things.InsufficientFundsException;
import org.example.Things.TransactionValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {
    private final SpringAccountRepository accountRepository;
    private final TransactionValidator validator;

    private final UserService userService;

    public AccountService(SpringAccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.validator = new TransactionValidator();
    }


    public Account getAccountById(String accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
    }

    public double getBalance(String accountId) throws AccountNotFoundException {
        Account account = getAccountOrThrow(accountId);
        return account.getBalance();
    }

    public void withdraw(String accountId, double amount) throws AccountNotFoundException, InsufficientFundsException {
        Account account = getAccountOrThrow(accountId);
        validator.validateWithdraw(amount, account.getBalance());
        account.setBalance(account.getBalance() - amount);
        account.addTransaction("Withdrawal: " + amount);
        accountRepository.save(account);
    }

    public void deposit(String accountId, double amount) throws AccountNotFoundException {
        Account account = getAccountOrThrow(accountId);
        validator.validateDeposit(amount);
        account.setBalance(account.getBalance() + amount);
        account.addTransaction("Deposit: " + amount);
        accountRepository.save(account);
    }

    public void transfer(String fromAccountId, String toAccountId, double amount)
            throws AccountNotFoundException, InsufficientFundsException {
        Account fromAccount = getAccountOrThrow(fromAccountId);
        Account toAccount = getAccountOrThrow(toAccountId);

        String fromOwner = String.valueOf(fromAccount.getOwnerLogin());
        String toOwner = String.valueOf(toAccount.getOwnerLogin());
        User fromUser = userService.getUserInfo(fromOwner);
        boolean isFriend = fromUser != null && fromUser.getFriends().contains(toOwner);

        double commission = calculateCommission(fromAccountId, toAccountId, isFriend, amount);
        double totalAmount = amount + commission;

        validator.validateTransfer(totalAmount, fromAccount.getBalance());

        fromAccount.setBalance(fromAccount.getBalance() - totalAmount);
        fromAccount.addTransaction("Transfer to " + toAccountId + ": " + amount + " (Commission: " + commission + ")");
        toAccount.setBalance(toAccount.getBalance() + amount);
        toAccount.addTransaction("Received from " + fromAccountId + ": " + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    private Account getAccountOrThrow(String accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
    }

    public double calculateCommission(String fromAccountId, String toAccountId, boolean isFriend, double amount) {
        String fromOwner = String.valueOf(accountRepository.findById(fromAccountId).orElseThrow().getOwnerLogin());
        String toOwner = String.valueOf(accountRepository.findById(toAccountId).orElseThrow().getOwnerLogin());

        if (fromOwner.equals(toOwner)) {
            return 0.0;
        }
        return isFriend ? amount * 0.03 : amount * 0.10;
    }


    public String getTransactionHistory(String accountId) throws AccountNotFoundException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        return account.getTransactionHistory().stream()
                .map(Transaction::getDescription)
                .collect(Collectors.joining("\n"));
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public AccountDTO createAccount(String ownerLogin) {
        Account account = new Account();
        account.setOwnerLogin(ownerLogin);
        accountRepository.save(account);
        return AccountMapper.toDTO(account);
    }

    public List<AccountDTO> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<TransactionDTO> getTransactions(String accountId) {
        List<Transaction> transactions = accountRepository.findTransactionsByAccountId(accountId);
        return transactions.stream()
                .map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<Account> getAccountsByUserId(String userId) {
        return accountRepository.findAccountsByUserId(Long.valueOf(userId));
    }

    public List<Account> getAccountsByOwnerLogin(String ownerLogin) {
        return accountRepository.findByOwnerLogin(ownerLogin);
    }
}