package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
