package com.bank.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EmployerRequest {
    private long id;

    @Length(min = 3,
            message = "Name should be at least 3 characters"
    )
    private String name;

    @Length(min = 3,
            message = "Address should be at least 3 characters"
    )
    private String address;
}
