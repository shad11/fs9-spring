package com.bank.dto;

import lombok.Data;

@Data
public class AccountRequest {
    private long id;
    private String number;
    private String currency;
    // баланс рахунку може бути негативним
    // if we accept any value (positive, negative, or zero), no validation annotation is required for range checks
    private double balance;
    // for transferring money
    private double amount;
}
