package com.bank.dto;

import com.bank.validation.FullUpdate;
import com.bank.validation.PartialUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is required", groups = {FullUpdate.class})
    @Email(message = "Email should be valid", groups = {FullUpdate.class, PartialUpdate.class})
    private String email;

    @NotBlank(message = "Username is required", groups = {FullUpdate.class})
    @Length(min = 2,
            message = "Username should be at least 2 characters",
            groups = {FullUpdate.class, PartialUpdate.class}
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Length(min = 8,
            message = "Password should be at least 8 characters"
    )
    private String password;
}
