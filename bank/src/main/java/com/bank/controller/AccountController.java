package com.bank.controller;

import com.bank.dto.AccountTransferDTO;
import com.bank.dto.MessageResponse;
import com.bank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

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

    @PatchMapping("/transfer")
    public ResponseEntity<MessageResponse> transfer(@RequestBody AccountTransferDTO accountTransferDTO) {
        String fromNumber = accountTransferDTO.getFromNumber();
        String toNumber = accountTransferDTO.getToNumber();
        double amount = accountTransferDTO.getAmount();

        if (amount <= 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Amount should be greater than 0"));
        }

        accountService.withdraw(fromNumber, amount);
        accountService.deposit(toNumber, amount);

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