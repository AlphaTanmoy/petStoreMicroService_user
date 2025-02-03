package com.store.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMqRequestForOtpDeliver {

    @NotNull(message = "Message cannot be null")
    @NotBlank(message = "Message cannot be empty")
    @Size(min = 5, max = 500, message = "Message must be between 5 and 500 characters")
    public String message;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid Email address")
    public String email;

    @NotNull(message = "OTP cannot be null")
    @NotBlank(message = "OTP cannot be empty")
    @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be a 6-digit numeric value")
    public String otp;

    @NotNull(message = "Subject cannot be null")
    @NotBlank(message = "Subject cannot be empty")
    @Size(min = 3, max = 100, message = "Subject must be between 3 and 100 characters")
    public String subject;
}
