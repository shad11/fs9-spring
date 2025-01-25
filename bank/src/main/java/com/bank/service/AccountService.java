package com.bank.service;

import com.bank.dto.AccountFacade;
import com.bank.dto.AccountRequest;
import com.bank.dto.AccountResponse;
import com.bank.entity.Customer;
import com.bank.exception.CustomerException;
import com.bank.entity.Account;
import com.bank.repository.AccountRepository;
import com.bank.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountFacade accountFacade;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, AccountFacade accountFacade) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountFacade = accountFacade;
    }

    public List<AccountResponse> getAll() {
        return accountRepository.findAll().stream()
                .map(accountFacade::toResponse)
                .toList();
    }

    public AccountResponse getById(long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new Error("Account not found"));

        return accountFacade.toResponse(account);
    }

    public AccountResponse addAccount(long customerId, AccountRequest accountRequest) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerException("Customer not found"));
        Account account = accountFacade.toEntity(accountRequest);
        // !! Check currency

        account.setCustomer(customer);
        Account savedAccount = accountRepository.save(account);

        return accountFacade.toResponse(savedAccount);
    }

    public void delete(long id) {
        accountRepository.deleteById(id);
    }

    public AccountResponse deposit(String accountNumber, double amount) {
        Account account = getByNumber(accountNumber);
        Account savedAccount = changeBalance(account, amount);

        return accountFacade.toResponse(savedAccount);
    }

    public AccountResponse withdraw(String accountNumber, double amount) {
        Account account = getByNumber(accountNumber);

        if (account.getBalance() < amount) {
            throw new CustomerException("Not enough money");
        }

        Account savedAccount = changeBalance(account, -1 * amount);

        return accountFacade.toResponse(savedAccount);
    }

    private Account getByNumber(String number) {
        Account account = accountRepository.findByNumber(number);

        if (account == null) {
            throw new CustomerException("Account not found");
        }

        return account;
    }

    private Account changeBalance(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);

        return accountRepository.save(account);
    }
}