package com.bank.controller;

import com.bank.dto.AccountRequest;
import com.bank.dto.AccountResponse;
import com.bank.service.AccountService;
import com.bank.service.WebSocketService;
import com.bank.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final WebSocketService webSocketService;

    @PostMapping("/{number}/deposit")
    public ResponseEntity<Object> increaseAccount(@PathVariable String number, @RequestParam("amount") double amount) {
        if (amount <= 0) {
            logNegativeAmount(amount);

            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Amount should be greater than 0",
                    null
            );
        }

        log.info("Depositing {} to account {}", amount, number);

        AccountResponse accountUpdated = accountService.deposit(number, amount);

        webSocketService.sendAccountUpdate(number, accountUpdated.getBalance());

        return ResponseEntity.ok(accountUpdated);
    }

    @PostMapping("/{number}/withdraw")
    public ResponseEntity<Object> decreaseAccount(@PathVariable String number, @RequestParam("amount") double amount) {
        if (amount <= 0) {
            logNegativeAmount(amount);

            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Amount should be greater than 0",
                    null
            );
        }

        log.info("Withdrawing {} from account {}", amount, number);

        AccountResponse accountUpdated = accountService.withdraw(number, amount);

        webSocketService.sendAccountUpdate(number, accountUpdated.getBalance());

        return ResponseEntity.ok(accountUpdated);
    }

    @PostMapping("/{number}/transfer")
    public ResponseEntity<Object> transfer(@PathVariable String number, @RequestBody AccountRequest accountRequest) {
        double amount = accountRequest.getAmount();

        if (amount <= 0) {
            logNegativeAmount(amount);

            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Amount should be greater than 0",
                    null
            );
        }

        AccountResponse accountFrom = accountService.withdraw(number, amount);
        AccountResponse accountTo = accountService.deposit(accountRequest.getNumber(), amount);

        webSocketService.sendAccountUpdate(accountFrom.getNumber(), accountFrom.getBalance());
        webSocketService.sendAccountUpdate(accountTo.getNumber(), accountTo.getBalance());

        log.info(
                "Transferring {} from account {} to account {}",
                amount,
                number,
                accountRequest.getNumber()
        );

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                false,
                "Transfer successful",
                null
        );
    }

    private void logNegativeAmount(double amount) {
        log.info("Amount {} should be greater than 0", amount);
    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
//        accountService.delete(id);
//
//        return ResponseEntity.noContent().build();
//    }
}