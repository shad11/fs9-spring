package com.bank.controller;

import com.bank.dto.*;
import com.bank.service.AccountService;
import com.bank.service.CustomerService;
import com.bank.validation.FullUpdate;
import com.bank.validation.PartialUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return ResponseEntity.ok(customerService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated(FullUpdate.class) CustomerRequest customerRequest) {
        if (customerRequest.getName() == null || customerRequest.getEmail() == null || customerRequest.getAge() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Customer name, email and age are mandatory"));
        }

        if (customerService.getByEmail(customerRequest.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already exists"));
        }

        return ResponseEntity.status(201).body(customerService.save(customerRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable long id, @RequestBody @Validated(PartialUpdate.class) CustomerRequest customerRequest) {
        return ResponseEntity.ok(customerService.update(id, customerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        customerService.delete(id);

        return ResponseEntity.ok(new MessageResponse("Customer deleted"));
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<Object> addAccount(@PathVariable("id") long customerId, @RequestBody AccountRequest accountRequest) {
        if (accountRequest.getCurrency() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Currency is mandatory"));
        }

        return ResponseEntity.ok(accountService.addAccount(customerId, accountRequest));
    }

    @DeleteMapping("/{id}/accounts/{accountId}")
    public ResponseEntity<Object> deleteAccount(@PathVariable long id, @PathVariable long accountId) {
        accountService.delete(accountId);

        return ResponseEntity.ok(new MessageResponse("Account deleted"));
    }
}