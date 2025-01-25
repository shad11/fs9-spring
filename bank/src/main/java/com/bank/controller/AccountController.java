package com.bank.controller;

import com.bank.dto.AccountRequest;
import com.bank.dto.MessageResponse;
import com.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PatchMapping("/{number}/deposit")
    public ResponseEntity<Object> increaseAccount(@PathVariable String number, @RequestParam("amount") double amount) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Amount should be greater than 0"));
        }

        return ResponseEntity.ok(accountService.deposit(number, amount));
    }

    @PatchMapping("/{number}/withdraw")
    public ResponseEntity<Object> decreaseAccount(@PathVariable String number, @RequestParam("amount") double amount) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Amount should be greater than 0"));
        }

        return ResponseEntity.ok(accountService.withdraw(number, amount));
    }

    @PatchMapping("/{number}/transfer")
    public ResponseEntity<MessageResponse> transfer(@PathVariable String number, @RequestBody AccountRequest accountRequest) {
        double amount = accountRequest.getAmount();

        if (amount <= 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Amount should be greater than 0"));
        }

        accountService.withdraw(number, amount);
        accountService.deposit(accountRequest.getNumber(), amount);

        return ResponseEntity.ok(new MessageResponse("Transfer successful"));
    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
//        accountService.delete(id);
//
//        return ResponseEntity.noContent().build();
//    }
}