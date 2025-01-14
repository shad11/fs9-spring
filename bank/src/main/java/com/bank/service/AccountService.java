package com.bank.service;

import com.bank.exception.CustomerException;
import com.bank.entity.Account;
import com.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account getById(long id) {
        return accountRepository.findById(id).orElseThrow(() -> new Error("Account not found"));
    }

    public Account getByNumber(String number) {
        Account account = accountRepository.findByNumber(number);

        if (account == null) {
            throw new CustomerException("Account not found");
        }

        return account;
    }

    public void delete(long id) {
        accountRepository.deleteById(id);
    }

    public Account deposit(String accountNumber, double amount) {
        Account account = getByNumber(accountNumber);

        account.setBalance(account.getBalance() + amount);

        return accountRepository.save(account);
    }

    public Account withdraw(String accountNumber, double amount) {
        Account account = getByNumber(accountNumber);

        if (account.getBalance() < amount) {
            throw new CustomerException("Not enough money");
        }

        account.setBalance(account.getBalance() - amount);

        return accountRepository.save(account);
    }
}