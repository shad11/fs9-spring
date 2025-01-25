package com.bank.dto;

import com.bank.enums.Currency;
import lombok.Data;

@Data
public class AccountResponse {
    private long id;
    private String number;
    private Currency currency;
    private double balance;
}