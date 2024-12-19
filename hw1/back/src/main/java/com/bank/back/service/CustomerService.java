package com.bank.back.service;

import com.bank.back.dao.AccountDAO;
import com.bank.back.dao.CustomerDAO;
import com.bank.back.exception.CustomerException;
import com.bank.back.model.Account;
import com.bank.back.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final AccountDAO accountDao;
    private final CustomerDAO customerDao;

    public CustomerService(AccountDAO accountDao, CustomerDAO customerDao) {
        this.accountDao = accountDao;
        this.customerDao = customerDao;
    }

    public Customer save(Customer customer) {
        return customerDao.save(customer);
    }

    public List<Customer> getAll() {
        return customerDao.findAll();
    }

    public Customer getById(long id) {
        Customer customer = customerDao.getOne(id);

        if (customer == null) {
            throw new CustomerException("Customer not found");
        }

        return customer;
    }

    public Customer getByEmail(String email) {
        return customerDao.findByEmail(email);
    }

    public boolean delete(Long id) {
        boolean isDeleted = customerDao.deleteById(id);

        if (!isDeleted) {
            throw new CustomerException("Customer not found");
        }

        return true;
    }

    public Customer addAccount(Long customerId, Account account) {
        Customer customer = customerDao.getOne(customerId);

        if (customer == null) {
            throw new CustomerException("Customer not found");
        }

        customer.getAccounts().add(account);

        return customer;
    }

    public void deleteAccount(Long customerId, Long accountId) {
        Customer customer = customerDao.getOne(customerId);

        if (customer == null) {
            throw new CustomerException("Customer not found");
        }

        Account account = accountDao.getOne(accountId);

        if (account == null) {
            throw new CustomerException("Account not found");
        }

        customer.getAccounts().remove(account);
    }
}
