package com.store.user.model;

import com.store.user.model.superEntity.SuperEntityWithoutExpiry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

import static com.store.user.config.KeywordsAndConstants.OTP_EXPIRED_IN_MINUTES;


@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "customer_verification_code")
public class CustomerVerificationCode extends SuperEntityWithoutExpiry {

    private String otp;

    private String email;

    @OneToOne
    private Customer customer;

    @Column(nullable = false)
    private ZonedDateTime expiryDate =ZonedDateTime.now().plusMinutes(OTP_EXPIRED_IN_MINUTES);
}
