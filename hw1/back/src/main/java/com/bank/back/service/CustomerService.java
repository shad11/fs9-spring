package com.bank.back.service;

import com.bank.back.exception.CustomerException;
import com.bank.back.model.Customer;
import com.bank.back.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerException("Customer not found"));
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

//    public Customer update(Long id, Customer customer) {
//        Customer customerToUpdate = getById(id);
//
//        customerToUpdate.setName(customer.getName());
//        customerToUpdate.setEmail(customer.getEmail());
//        customerToUpdate.setAge(customer.getAge());
//        customerToUpdate.setAccounts(customer.getAccounts());
//        return customerRepository.save(customerToUpdate);
//    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}
