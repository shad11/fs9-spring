package com.bank.back.controller;

import com.bank.back.dto.AccountTransferDTO;
import com.bank.back.model.Account;
import com.bank.back.entity.MessageResponse;
import com.bank.back.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PatchMapping("/{id}/deposit")
    public ResponseEntity<Account> increaseAccount(@PathVariable Long id, @RequestParam("amount") int amount) {
        Account account = accountService.getAccountById(id);

        account.setBalance(account.getBalance() + amount);

        return ResponseEntity.ok(accountService.saveAccount(account));
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<Object> decreaseAccount(@PathVariable Long id, @RequestParam("amount") int amount) {
        Account account = accountService.getAccountById(id);

        if (account.getBalance() < amount) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MessageResponse("Not enough money"));
        }

        account.setBalance(account.getBalance() - amount);

        return ResponseEntity.ok(accountService.saveAccount(account));
    }

    @PatchMapping("/transfer")
    public ResponseEntity<MessageResponse> transfer(@RequestBody AccountTransferDTO accountTransferDTO) {
        Account accountFrom = accountService.getAccountById(accountTransferDTO.getFromAccountId());
        Account accountTo = accountService.getAccountById(accountTransferDTO.getToAccountId());

        if (accountFrom.getBalance() < accountTransferDTO.getAmount()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MessageResponse("Not enough money"));
        }

        accountFrom.setBalance(accountFrom.getBalance() - accountTransferDTO.getAmount());
        accountTo.setBalance(accountTo.getBalance() + accountTransferDTO.getAmount());

        accountService.saveAccount(accountFrom);
        accountService.saveAccount(accountTo);

        return ResponseEntity.ok(new MessageResponse("Transfer successful"));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
//        return ResponseEntity.ok(accountService.getAccountById(id));
//    }
//
//    @PostMapping
//    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
//        return ResponseEntity.status(201).body(accountService.saveAccount(account));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
//        accountService.deleteAccount(id);
//        return ResponseEntity.noContent().build();
//    }
}
