package com.bank.entity;

import com.bank.enums.Currency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "accounts")
public class Account extends AbstractEntity {
    @UuidGenerator
    private String number;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(columnDefinition = "double default 0")
    private double balance;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    public Account() {
    }

    public Account(Currency currency, Customer customer) {
        this.currency = currency;
        this.customer = customer;
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
}