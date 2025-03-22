package com.bank.dto;

import com.bank.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private long id;
    private String number;
    private Currency currency;
    private double balance;
}