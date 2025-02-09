package com.store.user.collection;

import com.store.user.model.Coupon;
import com.store.user.model.CustomerAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserFullDetails {
    String id;
    String fullName;
    String emailId;
    String tireCode;
    String userRole;
    Boolean isPrimeMember;
    Set<Coupon> coupons;
    Set<CustomerAddress> addresses;
    private Instant createdDate;
}
