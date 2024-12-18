package com.bank.back.dao;

import com.bank.back.model.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountDAO implements DAO<Account> {
    private static DAO<Account> accountDAO = null;
    private final Map<Long, Account> accounts = new HashMap<>();

    private AccountDAO() {}

    public static DAO<Account> getInstance() {
        if (accountDAO == null) {
            accountDAO = new AccountDAO();
        }

        return accountDAO;
    }

    @Override
    public Account save(Account obj) {
        accounts.put(obj.getId(), obj);

        return obj;
    }

    @Override
    public boolean delete(Account obj) {
        return accounts.remove(obj.getId()) != null;
    }

    @Override
    public void deleteAll(List<Account> entities) {
        entities.forEach(account -> accounts.remove(account.getId()));
    }

    @Override
    public void saveAll(List<Account> entities) {
        entities.forEach(customer -> accounts.put(customer.getId(), customer));
    }

    @Override
    public List<Account> findAll() {
        return accounts.values().stream().toList();
    }

    @Override
    public boolean deleteById(long id) {
        return accounts.remove(id) != null;
    }

    @Override
    public Account getOne(long id) {
        return accounts.get(id);
    }

    public Account findByNumber(String number) {
        return accounts.values().stream().filter(account -> account.getNumber().equals(number)).findFirst().orElse(null);
    }
}
