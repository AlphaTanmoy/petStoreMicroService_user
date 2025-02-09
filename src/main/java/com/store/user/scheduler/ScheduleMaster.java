package com.store.user.scheduler;

import com.store.user.model.CustomerVerificationCode;
import com.store.user.repo.CustomerInfoLoggerRepository;
import com.store.user.repo.CustomerVerificationCodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduleMaster{

    private final CustomerVerificationCodeRepository CustomerVerificationCodeRepository;
    private final CustomerInfoLoggerRepository customerInfoLoggerRepository;

    public ScheduleMaster(
            CustomerVerificationCodeRepository CustomerVerificationCodeRepository,
            CustomerInfoLoggerRepository customerInfoLoggerRepository
    ) {
        this.CustomerVerificationCodeRepository = CustomerVerificationCodeRepository;
        this.customerInfoLoggerRepository = customerInfoLoggerRepository;
    }

    @Scheduled(cron = "0 * * * * ?") // every 1 mint
    public void deleteExpiredOtp() {
        LocalDateTime now = LocalDateTime.now();
        List<CustomerVerificationCode> expiredOtpList = CustomerVerificationCodeRepository.findByExpiryDateBefore(now);
        for (CustomerVerificationCode CustomerVerificationCode : expiredOtpList) {
            CustomerVerificationCodeRepository.delete(CustomerVerificationCode);
        }
        System.out.println("Found >> "+expiredOtpList.size()+" expired OTPs");
    }

    @Scheduled(cron = "0 0 0 * * ?")  // This runs every day at midnight
    public void remove24HoursLog() {
        customerInfoLoggerRepository.deleteAll();
    }
}
