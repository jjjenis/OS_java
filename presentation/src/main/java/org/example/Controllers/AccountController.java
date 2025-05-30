package org.example.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.DTOs.AccountDTO;
import org.example.DTOs.TransactionDTO;
import org.example.Mappers.AccountMapper;
import org.example.Entities.Account;
import org.example.Requests.CreateAccountRequest;
import org.example.Requests.DepositRequest;
import org.example.Requests.TransferRequest;
import org.example.Requests.WithdrawRequest;
import org.example.Services.AccountService;
import org.example.Things.AccountNotFoundException;
import org.example.Things.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "Endpoints for managing accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @Operation(summary = "Create a new account", description = "Creates a new account for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody CreateAccountRequest request) {
        AccountDTO accountDTO = accountService.createAccount(request.getOwnerLogin());
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
    }

    @Operation(summary = "Deposit money into an account", description = "Deposits the specified amount into the account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money deposited successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<Void> deposit(
            @PathVariable String accountId,
            @RequestBody DepositRequest request
    ) throws AccountNotFoundException {
        accountService.deposit(accountId, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(@RequestParam String ownerLogin) {
        AccountDTO account = accountService.createAccount(ownerLogin);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Withdraw money from an account", description = "Withdraws the specified amount from the account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money withdrawn successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "400", description = "Insufficient funds")
    })
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<Void> withdraw(
            @PathVariable String accountId,
            @RequestBody WithdrawRequest request
    ) throws AccountNotFoundException, InsufficientFundsException {
        accountService.withdraw(accountId, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get account balance", description = "Returns the current balance of the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable String accountId) throws AccountNotFoundException {
        double balance = accountService.getBalance(accountId);
        return ResponseEntity.ok(balance);
    }

    @Operation(summary = "Get all accounts", description = "Returns a list of all accounts in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get transactions for an account", description = "Returns a list of transactions for the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable String accountId) {
        List<TransactionDTO> transactions = accountService.getTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Transfer money between accounts", description = "Transfers money from one account to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money transferred successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "400", description = "Insufficient funds or invalid input")
    })
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) throws AccountNotFoundException, InsufficientFundsException {
        accountService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Calculate commission for a transfer", description = "Calculates the commission for transferring money between accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commission calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @GetMapping("/commission")
    public ResponseEntity<Double> calculateCommission(
            @RequestParam String fromAccountId,
            @RequestParam String toAccountId,
            @RequestParam double amount,
            @RequestParam boolean isFriend
    ) {
        double commission = accountService.calculateCommission(fromAccountId, toAccountId, isFriend, amount);
        return ResponseEntity.ok(commission);
    }

    @Operation(summary = "Get account by ID", description = "Returns detailed information about the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable String accountId) throws AccountNotFoundException {
        Account account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(AccountMapper.toDTO(account));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable String userId) {
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }
}