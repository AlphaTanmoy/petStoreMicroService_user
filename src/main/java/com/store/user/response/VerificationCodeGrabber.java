package com.store.user.response;

import com.store.user.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeGrabber {
    private String otp;
    private String email;
    private Users user;
    private LocalDateTime expiryDate;
}
