package com.store.user.scheduler;

import com.store.user.model.CustomerVerificationCode;
import com.store.user.repo.CustomerInfoLoggerRepository;
import com.store.user.repo.CustomerVerificationCodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class ScheduleMaster{

    private final CustomerVerificationCodeRepository customerVerificationCodeRepository;
    private final CustomerInfoLoggerRepository customerInfoLoggerRepository;
    private final MicroServiceChecker microServiceChecker;

    public ScheduleMaster(
            CustomerVerificationCodeRepository customerVerificationCodeRepository,
            CustomerInfoLoggerRepository customerInfoLoggerRepository,
            MicroServiceChecker microServiceChecker
    ) {
        this.customerVerificationCodeRepository = customerVerificationCodeRepository;
        this.customerInfoLoggerRepository = customerInfoLoggerRepository;
        this.microServiceChecker = microServiceChecker;
    }

    @Scheduled(cron = "0 * * * * ?") // every 1 mint
    public void deleteExpiredOtp() {
        ZonedDateTime now = ZonedDateTime.now();
        List<CustomerVerificationCode> expiredOtpList = customerVerificationCodeRepository.findByExpiryDateBefore(now);
        for (CustomerVerificationCode CustomerVerificationCode : expiredOtpList) {
            customerVerificationCodeRepository.delete(CustomerVerificationCode);
        }
        System.out.println("Found >> "+expiredOtpList.size()+" expired OTPs");
    }

    @Scheduled(cron = "0 */2 * * * ?")
    public void runEvery5Minutes(){
        microServiceChecker.checkMicroServices();
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void runEvery5Hours(){}

    @Scheduled(cron = "0 0 0 * * ?")  // This runs every day at midnight
    public void remove24HoursLog() {
        customerInfoLoggerRepository.deleteAll();
    }
}
