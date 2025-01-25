package com.bank.service;

import com.bank.dto.AccountRequest;
import com.bank.dto.CustomerFacade;
import com.bank.dto.CustomerRequest;
import com.bank.dto.CustomerResponse;
import com.bank.exception.CustomerException;
import com.bank.entity.Customer;
import com.bank.entity.Employer;
import com.bank.repository.CustomerRepository;
import com.bank.repository.EmployerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerFacade customerFacade;
    private final EmployerRepository employerRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerFacade customerFacade, EmployerRepository employerRepository) {
        this.customerRepository = customerRepository;
        this.customerFacade = customerFacade;
        this.employerRepository = employerRepository;
    }

    public Page<CustomerResponse> getAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerFacade::toResponse);
    }

    public CustomerResponse getById(long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerException("Customer not found"));

        return customerFacade.toResponse(customer);
    }

    public CustomerResponse getByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            return null;
        }

        return customerFacade.toResponse(customer);
    }

    public CustomerResponse save(CustomerRequest customerRequest) {
        Customer customer = customerFacade.toEntity(customerRequest);
        Customer savedCustomer = customerRepository.save(customer);

        return customerFacade.toResponse(savedCustomer);
    }

    public CustomerResponse update(long id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerException("Customer not found"));

        if (customerRequest.getEmail() != null) {
            customer.setEmail(customerRequest.getEmail());
        }

        if (customerRequest.getPassword() != null) {
            customer.setPassword(customerRequest.getPassword());
        }

        if (customerRequest.getName() != null) {
            customer.setName(customerRequest.getName());
        }

        if (customerRequest.getAge() != null) {
            customer.setAge(customerRequest.getAge());
        }

        if (customerRequest.getPhone() != null) {
            customer.setPhone(customerRequest.getPhone());
        }

        Customer savedCustomer = customerRepository.save(customer);

        return customerFacade.toResponse(savedCustomer);
    }

    public void addEmployerToCustomer(long customerId, long employerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerException("Customer not found"));
        Employer employer = employerRepository.findById(employerId).orElseThrow(() -> new CustomerException("Employer not found"));

        customer.getEmployers().add(employer);
        customerRepository.save(customer);
    }

    //    public Customer update(long id, Customer customer) {
//        Customer customerToUpdate = getById(id);
//
//        customerToUpdate.setName(customer.getName());
//        customerToUpdate.setEmail(customer.getEmail());
//        customerToUpdate.setAge(customer.getAge());
//        customerToUpdate.setAccounts(customer.getAccounts());
//        return customerRepository.save(customerToUpdate);
//    }
    public void delete(long id) {
        customerRepository.deleteById(id);
    }
}