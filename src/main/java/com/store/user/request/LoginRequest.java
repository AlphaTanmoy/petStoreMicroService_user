package com.store.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotNull(message = "Please provide an Email")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid Email address")
    private String email;

    @NotNull(message = "Please provide a Password")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be a 6-digit numeric value")
    private String otp;
}
