package com.store.user.response;

import com.store.user.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerVerificationCodeGrabber {
    private String otp;
    private String email;
    private Customer customer;
    private LocalDateTime expiryDate;
}
