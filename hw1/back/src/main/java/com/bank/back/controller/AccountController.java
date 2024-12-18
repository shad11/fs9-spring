package com.bank.back.controller;

import com.bank.back.dto.AccountTransferDTO;
import com.bank.back.model.Account;
import com.bank.back.entity.MessageResponse;
import com.bank.back.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PatchMapping("/deposit")
    public ResponseEntity<Account> increaseAccount(@RequestParam("number") String number, @RequestParam("amount") int amount) {
        Account account = accountService.getByNumber(number);

        account.setBalance(account.getBalance() + amount);

        return ResponseEntity.ok(accountService.save(account));
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<Object> decreaseAccount(@RequestParam("number") String number, @RequestParam("amount") int amount) {
        Account account = accountService.getByNumber(number);

        if (account.getBalance() < amount) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MessageResponse("Not enough money"));
        }

        account.setBalance(account.getBalance() - amount);

        return ResponseEntity.ok(accountService.save(account));
    }

    @PatchMapping("/transfer")
    public ResponseEntity<MessageResponse> transfer(@RequestBody AccountTransferDTO accountTransferDTO) {
        Account accountFrom = accountService.getByNumber(accountTransferDTO.getFromNumber());
        Account accountTo = accountService.getByNumber(accountTransferDTO.getToNumber());

        if (accountFrom.getBalance() < accountTransferDTO.getAmount()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MessageResponse("Not enough money"));
        }

        accountFrom.setBalance(accountFrom.getBalance() - accountTransferDTO.getAmount());
        accountTo.setBalance(accountTo.getBalance() + accountTransferDTO.getAmount());

        accountService.save(accountFrom);
        accountService.save(accountTo);

        return ResponseEntity.ok(new MessageResponse("Transfer successful"));
    }

//    @PatchMapping("/{id}/deposit")
//    public ResponseEntity<Account> increaseAccount(@PathVariable Long id, @RequestParam("amount") int amount) {
//        Account account = accountService.getById(id);
//
//        account.setBalance(account.getBalance() + amount);
//
//        return ResponseEntity.ok(accountService.save(account));
//    }
//
//    @PatchMapping("/{id}/withdraw")
//    public ResponseEntity<Object> decreaseAccount(@PathVariable Long id, @RequestParam("amount") int amount) {
//        Account account = accountService.getById(id);
//
//        if (account.getBalance() < amount) {
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MessageResponse("Not enough money"));
//        }
//
//        account.setBalance(account.getBalance() - amount);
//
//        return ResponseEntity.ok(accountService.save(account));
//    }
//
//    @PatchMapping("/transfer")
//    public ResponseEntity<MessageResponse> transfer(@RequestBody AccountTransferDTO accountTransferDTO) {
//        Account accountFrom = accountService.getById(accountTransferDTO.getFromAccountId());
//        Account accountTo = accountService.getById(accountTransferDTO.getToAccountId());
//
//        if (accountFrom.getBalance() < accountTransferDTO.getAmount()) {
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MessageResponse("Not enough money"));
//        }
//
//        accountFrom.setBalance(accountFrom.getBalance() - accountTransferDTO.getAmount());
//        accountTo.setBalance(accountTo.getBalance() + accountTransferDTO.getAmount());
//
//        accountService.save(accountFrom);
//        accountService.save(accountTo);
//
//        return ResponseEntity.ok(new MessageResponse("Transfer successful"));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
//        accountService.delete(id);
//
//        return ResponseEntity.noContent().build();
//    }
}
