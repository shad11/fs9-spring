package com.bank.service;

import com.bank.exception.CustomerException;
import com.bank.entity.Customer;
import com.bank.entity.Employer;
import com.bank.repository.CustomerRepository;
import com.bank.repository.EmployerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final EmployerRepository employerRepository;

    public CustomerService(CustomerRepository customerRepository, EmployerRepository employerRepository) {
        this.customerRepository = customerRepository;
        this.employerRepository = employerRepository;
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer getById(long id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerException("Customer not found"));
    }

    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void addEmployerToCustomer(long customerId, long employerId) {
        Customer customer = getById(customerId);
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