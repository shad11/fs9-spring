package com.bank.controller;

import com.bank.dto.*;
import com.bank.service.CustomerService;
import com.bank.service.EmployerService;
import com.bank.validation.PartialUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid EmployerRequest employerRequest) {
        if (employerService.getEmployerByName(employerRequest.getName()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Employer already exists"));
        }

        return ResponseEntity.status(201).body(employerService.save(employerRequest));
    }

    @PostMapping("/{id}/add-customer")
    public ResponseEntity<Object> addCustomer(@PathVariable long id, @RequestBody @Validated(PartialUpdate.class) CustomerRequest customerRequest) {
        EmployerResponse employer = employerService.getById(id);

        if (customerRequest.getEmail() == null && customerRequest.getId() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Customer email or ID is mandatory"));
        }

        CustomerResponse customer = null;

        if (customerRequest.getId() != 0) {
            customer = customerService.getById(customerRequest.getId());
        } else {
            customer = customerService.getByEmail(customerRequest.getEmail());
        }

        if (customer == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Customer not found"));
        }

        customerService.addEmployerToCustomer(customer.getId(), employer.getId());

        return ResponseEntity.ok(new MessageResponse("Customer added to employer"));
    }
}
