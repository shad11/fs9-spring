package com.bank.dto;

import com.bank.entity.Account;
import com.bank.entity.Employer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private long id;
    private String name;
    private String email;
    private int age;
    private String phone;
    private List<Account> accounts;
    private Set<Employer> employers;
}