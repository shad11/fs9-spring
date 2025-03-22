package com.bank.controller;

import com.bank.dto.*;
import com.bank.service.AccountService;
import com.bank.service.CustomerService;
import com.bank.util.ResponseHandler;
import com.bank.validation.FullUpdate;
import com.bank.validation.PartialUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(customerService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated(FullUpdate.class) CustomerRequest customerRequest) {
//        if (customerRequest.getName() == null || customerRequest.getEmail() == null || customerRequest.getAge() == 0) {
//            return ResponseHandler.generateResponse(
//                    HttpStatus.BAD_REQUEST,
//                    true,
//                    "Customer name, email and age are mandatory",
//                    null
//            );
//        }
        if (customerService.getByEmail(customerRequest.getEmail()) != null) {
            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Email already exists",
                    null
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.save(customerRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable long id, @RequestBody @Validated(PartialUpdate.class) CustomerRequest customerRequest) {
        return ResponseEntity.ok(customerService.update(id, customerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        customerService.delete(id);

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
            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Currency is mandatory",
                    null
            );
        }

        return ResponseEntity.ok(accountService.addAccount(customerId, accountRequest));
    }

    @DeleteMapping("/{id}/accounts/{accountId}")
    public ResponseEntity<Object> deleteAccount(@PathVariable long id, @PathVariable long accountId) {
        accountService.delete(accountId);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                false,
                "Account deleted",
                null
        );
    }
}