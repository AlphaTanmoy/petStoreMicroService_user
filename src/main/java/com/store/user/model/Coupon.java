package com.store.user.model;

import com.store.user.model.superEntity.SuperEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Coupon extends SuperEntity {

    @Column(unique = true, nullable = false)
    private String code;

    private double discountPercentage;

    private LocalDate validityStartDate;

    private LocalDate validityEndDate;

    private double minimumOrderValue;

    private boolean isActive=true;

    @ManyToMany(mappedBy = "usedCoupons")
    private Set<Users> usedByUsers=new HashSet<>();

}
