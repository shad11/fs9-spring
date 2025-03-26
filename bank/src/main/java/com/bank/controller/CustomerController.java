package com.bank.controller;

import com.bank.dto.*;
import com.bank.service.AccountService;
import com.bank.service.CustomerService;
import com.bank.util.ResponseHandler;
import com.bank.validation.FullUpdate;
import com.bank.validation.PartialUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        log.info("Getting {} customers from page {}", pageable.getPageSize(), page);

        return ResponseEntity.ok(customerService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable long id) {
        CustomerResponse customer = customerService.getById(id);

        log.info(
                "Getting customer with ID {} and email {}",
                id,
                customer.getEmail()
        );

        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated(FullUpdate.class) CustomerRequest customerRequest) {
        if (customerService.getByEmail(customerRequest.getEmail()) != null) {
            log.info(
                    "Customer with email: {} can't be created. It already exists.",
                    customerRequest.getEmail()
            );

            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Email already exists",
                    null
            );
        }

        CustomerResponse customer = customerService.save(customerRequest);

        log.info(
                "Customer with email {} created successfully. Customer ID is {}",
                customer.getEmail(),
                customer.getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable long id, @RequestBody @Validated(PartialUpdate.class) CustomerRequest customerRequest) {
        CustomerResponse customerUpdated = customerService.update(id, customerRequest);

        log.info(
                "Customer with ID {} updated successfully. New customer data: {}",
                id,
                customerUpdated.toString()
        );

        return ResponseEntity.ok(customerUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        customerService.delete(id);

        log.info("Customer with ID {} deleted", id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                false,
                "Customer deleted",
                null
        );
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<Object> addAccount(@PathVariable("id") long customerId, @RequestBody AccountRequest accountRequest) {
        if (accountRequest.getCurrency() == null) {
            log.info("When creating an account currency is mandatory");

            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Currency is mandatory",
                    null
            );
        }

        AccountResponse accountResponse = accountService.addAccount(customerId, accountRequest);

        log.info(
                "Account with number {} created successfully for customer with ID {}",
                accountResponse.getNumber(),
                customerId
        );

        return ResponseEntity.ok(accountResponse);
    }

    @DeleteMapping("/{id}/accounts/{accountId}")
    public ResponseEntity<Object> deleteAccount(@PathVariable long id, @PathVariable long accountId) {
        accountService.delete(accountId);

        log.info("Account with ID {} deleted", accountId);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                false,
                "Account deleted",
                null
        );
    }
}