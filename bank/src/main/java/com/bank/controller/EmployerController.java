package com.bank.controller;

import com.bank.dto.*;
import com.bank.service.CustomerService;
import com.bank.service.EmployerService;
import com.bank.util.ResponseHandler;
import com.bank.validation.FullUpdate;
import com.bank.validation.PartialUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employers")
@RequiredArgsConstructor
@Validated
public class EmployerController {
    private final EmployerService employerService;
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<EmployerResponse>> getAll() {
        return ResponseEntity.ok(employerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployerResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(employerService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated(FullUpdate.class) EmployerRequest employerRequest) {
        if (employerService.getEmployerByName(employerRequest.getName()) != null) {
            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Employer already exists",
                    null
            );
        }

        return ResponseEntity.status(201).body(employerService.save(employerRequest));
    }

    @PostMapping("/{id}/add-customer")
    public ResponseEntity<Object> addCustomer(@PathVariable long id, @RequestBody @Validated(PartialUpdate.class) CustomerRequest customerRequest) {
        EmployerResponse employer = employerService.getById(id);

        if (customerRequest.getEmail() == null && customerRequest.getId() == 0) {
            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Customer email or ID is mandatory",
                    null
            );
        }

        CustomerResponse customer = null;

        if (customerRequest.getId() != 0) {
            customer = customerService.getById(customerRequest.getId());
        } else {
            customer = customerService.getByEmail(customerRequest.getEmail());
        }

        if (customer == null) {
            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Customer not found",
                    null
            );
        }

        customerService.addEmployerToCustomer(customer.getId(), employer.getId());

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                false,
                "Customer added to employer",
                null
        );
    }
}
