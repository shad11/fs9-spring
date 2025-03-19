package com.bank.dto;

import com.bank.validation.FullUpdate;
import com.bank.validation.PartialUpdate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    private long id;

    @Email(message = "Email should be valid",
            groups = {FullUpdate.class, PartialUpdate.class}
    )
    @NotNull(groups = FullUpdate.class)
    private String email;

    @Length(min = 8,
            message = "Password should be at least 8 characters",
            groups = {FullUpdate.class, PartialUpdate.class}
    )
    @NotNull(groups = FullUpdate.class)
    private String password;

    @Length(min = 2,
            message = "Name should be at least 2 characters",
            groups = {FullUpdate.class, PartialUpdate.class}
    )
    @NotNull(groups = FullUpdate.class)
    private String name;

    @Min(value = 18,
            message = "Customer should be at least 18 years old",
            groups = {FullUpdate.class, PartialUpdate.class}
    )
    private Integer age;

    @Pattern(regexp = "^[1-9]\\d{1,3}\\d{4,10}$",
            message = "Invalid phone number. It should follow the format: 1 to 3 digits for country code and 4 to 10 digits for the subscriber number.",
            groups = {FullUpdate.class, PartialUpdate.class}
    )
    private String phone;
}
