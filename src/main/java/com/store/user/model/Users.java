package com.store.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.store.user.enums.TIRE_CODE;
import com.store.user.enums.USER_ROLE;
import com.store.user.model.superEntity.SuperEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "users")
public class Users extends SuperEntity {

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String fullName;

    private String mobile;

    private USER_ROLE role;

    @OneToMany
    private Set<Address> addresses=new HashSet<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TIRE_CODE tireCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<UserLogs> devices = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_coupons",
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    private Set<Coupon> usedCoupons=new HashSet<>();

}
