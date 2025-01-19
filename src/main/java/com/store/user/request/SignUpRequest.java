package com.store.user.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String fullName;
    private String otp;
    private String mobile;
    private String locality;
    private String address;
    private String city;
    private String state;
    private String pinCode;
}
