package com.bank.back.controller;

import com.bank.back.dto.AccountDTO;
import com.bank.back.dto.CustomerDTO;
import com.bank.back.model.Account;
import com.bank.back.model.Customer;
import com.bank.back.serviceH2.AccountService;
import com.bank.back.serviceH2.CustomerService;
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
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        Customer customer = customerService.getById(id);

        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody CustomerDTO customerDTO) {
        Customer customer = new Customer(customerDTO.getName(), customerDTO.getEmail(), customerDTO.getAge());

        return ResponseEntity.status(201).body(customerService.save(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<Customer> addAccount(@PathVariable Long id, @RequestBody AccountDTO accountDTO) {
        Customer customer = customerService.getById(id);
        Account account = new Account(accountDTO.getCurrency(), customer);

        accountService.save(account);

        return ResponseEntity.ok(customerService.getById(id));
    }

    @DeleteMapping("/{id}/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id, @PathVariable Long accountId) {
        accountService.delete(accountId);

        return ResponseEntity.noContent().build();
    }
}
