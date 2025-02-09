package com.store.user.collection;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomers {
    String id;
    String fullName;
    String emailId;
    String tireCode;
    String userRole;
    Boolean isPrimeMember;
    private Instant createdDate;
}
