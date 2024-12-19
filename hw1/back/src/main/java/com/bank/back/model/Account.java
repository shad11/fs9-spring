package com.bank.back.model;

import com.bank.back.enums.Currency;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Objects;
import java.util.UUID;

public class Account {
    private long id = IdGenerator.generateAccountId();

    private String number = UUID.randomUUID().toString();

    private Currency currency;

    private double balance;

    @JsonBackReference
    private Customer customer;

    public Account(Currency currency, Customer customer) {
        this.currency = currency;
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Account account))
            return false;

        return account.getBalance() == balance &&
                account.getId() == id &&
                account.getNumber().equals(number) &&
                account.getCurrency().equals(currency) &&
                account.getCustomer().equals(customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, currency, balance, customer);
    }

    @Override
    public String toString() {
        return "Account { " +
                "number: '" + number + '\'' +
                ", currency: " + currency.name() +
                ", balance: " + balance +
                ", customer name: " + customer.getName() +
                ", customer email: " + customer.getEmail() +
                " }";
    }
}
