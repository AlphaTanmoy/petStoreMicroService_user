package com.store.user.repo;

import com.store.user.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,String> {
}
