package com.store.user.request;

import lombok.Data;

@Data
public class SaveOtpRequest {
    private String otp;
    private String email;
}
