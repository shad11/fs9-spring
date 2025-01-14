package com.bank.controller;

import com.bank.dto.AccountDTO;
import com.bank.dto.CustomerDTO;
import com.bank.dto.MessageResponse;
import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.service.AccountService;
import com.bank.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final AccountService accountService;

    public CustomerController(CustomerService customerService, AccountService accountService) {
        this.customerService = customerService;
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        List<CustomerDTO> customers =  customerService.getAll().stream().map(customer -> {
            CustomerDTO customerDTO = new CustomerDTO();

            customerDTO.setId(customer.getId());
            customerDTO.setName(customer.getName());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setAge(customer.getAge());

            return customerDTO;
        }).toList();

        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable long id) {
        Customer customer = customerService.getById(id);

        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CustomerDTO customerDTO) {
        if (customerDTO.getName() == null || customerDTO.getEmail() == null || customerDTO.getAge() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Customer name, email and age are mandatory"));
        }

        if (customerService.getByEmail(customerDTO.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already exists"));
        }

        Customer customer = new Customer(customerDTO.getName(), customerDTO.getEmail(), customerDTO.getAge());

        return ResponseEntity.status(201).body(customerService.save(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable long id, @RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.getById(id);

        if (customerDTO.getName() != null) {
            customer.setName(customerDTO.getName());
        }

        if (customerDTO.getEmail() != null) {
            customer.setEmail(customerDTO.getEmail());
        }

        if (customerDTO.getAge() != 0) {
            customer.setAge(customerDTO.getAge());
        }

        return ResponseEntity.ok(customerService.save(customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        customerService.delete(id);

        return ResponseEntity.ok(new MessageResponse("Customer deleted"));
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<Object> addAccount(@PathVariable long id, @RequestBody AccountDTO accountDTO) {
        if (accountDTO.getCurrency() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Currency is mandatory"));
        }

        Customer customer = customerService.getById(id);
        Account account = new Account(accountDTO.getCurrency(), customer);

        return ResponseEntity.ok(accountService.save(account));
    }

    @DeleteMapping("/{id}/accounts/{accountId}")
    public ResponseEntity<Object> deleteAccount(@PathVariable long id, @PathVariable long accountId) {
        accountService.delete(accountId);

        return ResponseEntity.ok(new MessageResponse("Account deleted"));
    }
}