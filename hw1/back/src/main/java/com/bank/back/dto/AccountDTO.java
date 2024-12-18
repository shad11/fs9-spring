package com.bank.back.dto;

import com.bank.back.enums.Currency;

public class AccountDTO {
    private Long id;
    private String number;
    private Currency currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setAccountNumber(String number) {
        this.number = number;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = Currency.valueOf(currency);
    }
}
