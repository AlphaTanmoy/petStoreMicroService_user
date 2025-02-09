package com.store.user.repo;

import com.store.user.model.CustomerVerificationCode;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface CustomerVerificationCodeRepository extends JpaRepository<CustomerVerificationCode, String> {

    @Query(
            value = "SELECT * FROM customer_verification_code " +
                    "WHERE email = :email"
            , nativeQuery = true
    )
    List<CustomerVerificationCode> findByEmail(@Param("email") String email);

    CustomerVerificationCode findByOtp(String otp);


    @Transactional
    @Modifying
    @Query(
            value = "UPDATE customer_verification_code SET user_id = :userId WHERE email = :email"
            , nativeQuery = true
    )
    void updateUserId(
            @Param("userId") String userId,
            @Param("email") String email
    );


    List<CustomerVerificationCode> findByExpiryDateBefore(ZonedDateTime now);
}

