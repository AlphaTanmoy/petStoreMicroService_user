package com.store.user.request;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class SignUpRequest {

    @NotNull(message = "Please provide an Email")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid Email address")
    private String email;

    @NotNull(message = "Please provide Full Name")
    @NotBlank(message = "Full Name cannot be empty")
    @Size(min = 2, max = 100, message = "Full Name must be between 2 and 100 characters")
    private String fullName;

    @NotNull(message = "Please provide an OTP")
    @NotBlank(message = "OTP cannot be empty")
    @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be a 6-digit numeric value")
    private String otp;

    @NotNull(message = "Please provide a Mobile Number")
    @NotBlank(message = "Mobile Number cannot be empty")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile Number must be a valid 10-digit number starting with 6-9")
    private String mobileNumber;
}

