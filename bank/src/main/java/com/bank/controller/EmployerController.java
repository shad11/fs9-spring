package com.bank.controller;

import com.bank.dto.CustomerDTO;
import com.bank.dto.EmployerDTO;
import com.bank.dto.MessageResponse;
import com.bank.entity.Customer;
import com.bank.entity.Employer;
import com.bank.service.CustomerService;
import com.bank.service.EmployerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employers")
public class EmployerController {
    private final EmployerService employerService;
    private final CustomerService customerService;

    public EmployerController(EmployerService employerService, CustomerService customerService) {
        this.employerService = employerService;
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<EmployerDTO>> getAll() {
        List<EmployerDTO> employers = employerService.getAll().stream().map(employer -> {
            EmployerDTO employerDTO = new EmployerDTO();

            employerDTO.setId(employer.getId());
            employerDTO.setName(employer.getName());
            employerDTO.setAddress(employer.getAddress());

            return employerDTO;
        }).toList();

        return ResponseEntity.ok(employers);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody EmployerDTO employerDTO) {
        if (employerDTO.getName() == null || employerDTO.getAddress() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Employer name and address are mandatory"));
        }

        if (employerService.getEmployerByName(employerDTO.getName()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Employer already exists"));
        }

        Employer employer = new Employer(employerDTO.getName(), employerDTO.getAddress());
        System.out.println(employer);

        return ResponseEntity.status(201).body(employerService.save(employer));
    }

    @PostMapping("/{id}/add-customer")
    public ResponseEntity<Object> addCustomer(@PathVariable long id, @RequestBody CustomerDTO customerDTO) {
        Employer employer = employerService.getById(id);

        if (employer == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Employer not found"));
        }

        if (customerDTO.getEmail() == null && customerDTO.getId() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Customer email or ID is mandatory"));
        }

        Customer customer = null;

        if (customerDTO.getId() != 0) {
            customer = customerService.getById(customerDTO.getId());
        } else {
            customer = customerService.getByEmail(customerDTO.getEmail());
        }

        if (customer == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Customer not found"));
        }

        customerService.addEmployerToCustomer(customer.getId(), employer.getId());

        return ResponseEntity.ok(new MessageResponse("Customer added to employer"));
    }
}
