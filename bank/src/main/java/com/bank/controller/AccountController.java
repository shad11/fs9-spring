package com.bank.controller;

import com.bank.dto.AccountRequest;
import com.bank.service.AccountService;
import com.bank.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Amount should be greater than 0",
                    null
            );
        }

        return ResponseEntity.ok(accountService.deposit(number, amount));
    }

    @PatchMapping("/{number}/withdraw")
    public ResponseEntity<Object> decreaseAccount(@PathVariable String number, @RequestParam("amount") double amount) {
        if (amount <= 0) {
            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Amount should be greater than 0",
                    null
            );
        }

        return ResponseEntity.ok(accountService.withdraw(number, amount));
    }

    @PatchMapping("/{number}/transfer")
    public ResponseEntity<Object> transfer(@PathVariable String number, @RequestBody AccountRequest accountRequest) {
        double amount = accountRequest.getAmount();

        if (amount <= 0) {
            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Amount should be greater than 0",
                    null
            );
        }

        accountService.withdraw(number, amount);
        accountService.deposit(accountRequest.getNumber(), amount);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                false,
                "Transfer successful",
                null
        );
    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
//        accountService.delete(id);
//
//        return ResponseEntity.noContent().build();
//    }
}