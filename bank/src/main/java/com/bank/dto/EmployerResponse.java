package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerResponse {
    private long id;
    private String name;
    private String address;
    private Set<CustomerResponse> customers = new HashSet<>();
}
