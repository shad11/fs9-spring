package com.bank.back.serviceH2;

import com.bank.back.model.Account;
import com.bank.back.repository.AccountRepository;
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

    public Account getById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new Error("Account not found"));
    }

    public Account getByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
