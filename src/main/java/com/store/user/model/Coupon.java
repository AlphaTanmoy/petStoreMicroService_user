package com.store.user.model;

import com.store.user.model.superEntity.SuperEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "coupon")
public class Coupon extends SuperEntity {

    @Column(unique = true, nullable = false)
    private String code;

    private double discountPercentage;

    private ZonedDateTime validityStartDate;

    private ZonedDateTime validityEndDate;

    private double minimumOrderValue;

    private boolean isActive=true;

    @ManyToMany(mappedBy = "usedCoupons")
    private Set<Customer> usedByUsers=new HashSet<>();

}
