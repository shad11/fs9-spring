package com.bank.back.service;

import com.bank.back.dao.AccountDAO;
import com.bank.back.exception.CustomerException;
import com.bank.back.model.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account save(Account account) {
        return accountDAO.save(account);
    }

    public boolean delete(long id) {
        boolean isDeleted = accountDAO.deleteById(id);

        if (!isDeleted) {
            throw new CustomerException("Account not found");
        }

        return true;
    }

    public Account getByNumber(String number) {
        Account account = accountDAO.findByNumber(number);

        if (account == null) {
            throw new CustomerException("Account not found");
        }

        return accountDAO.findByNumber(number);
    }
}
