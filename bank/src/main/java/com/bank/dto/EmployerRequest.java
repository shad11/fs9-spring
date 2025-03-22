package com.bank.dto;

import com.bank.validation.FullUpdate;
import com.bank.validation.PartialUpdate;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerRequest {
    private long id;

    @NotBlank(message = "Username is required", groups = {FullUpdate.class})
    @Length(min = 3,
            message = "Name should be at least 3 characters",
            groups = {FullUpdate.class, PartialUpdate.class}
    )
    private String name;

    @NotBlank(message = "Password is required")
    @Length(min = 3,
            message = "Address should be at least 3 characters",
            groups = {FullUpdate.class, PartialUpdate.class}
    )
    private String address;
}
