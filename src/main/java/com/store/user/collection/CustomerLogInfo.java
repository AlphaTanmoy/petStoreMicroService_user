package com.store.user.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLogInfo {
    private String customerId;
    private String ipAddress;
    private String operatingSystem;
    private String deviceType;
}
