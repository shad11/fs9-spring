package com.bank.back.controller;

import com.bank.back.dto.CustomerDTO;
import com.bank.back.model.Customer;
import com.bank.back.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
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
}
