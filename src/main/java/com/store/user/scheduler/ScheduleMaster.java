package com.store.user.scheduler;

import com.store.user.model.VerificationCode;
import com.store.user.repo.InfoLoggerRepository;
import com.store.user.repo.VerificationCodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduleMaster{

    private final VerificationCodeRepository verificationCodeRepository;
    private final InfoLoggerRepository infoLoggerRepository;
    private final ApiKeyService apiKeyService;

    public ScheduleMaster(
            VerificationCodeRepository verificationCodeRepository,
            InfoLoggerRepository infoLoggerRepository,
            ApiKeyService apiKeyService
    ) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.infoLoggerRepository = infoLoggerRepository;
        this.apiKeyService = apiKeyService;
    }

    @Scheduled(cron = "0 * * * * ?") // every 1 mint
    public void deleteExpiredOtp() {
        LocalDateTime now = LocalDateTime.now();
        List<VerificationCode> expiredOtpList = verificationCodeRepository.findByExpiryDateBefore(now);
        for (VerificationCode verificationCode : expiredOtpList) {
            verificationCodeRepository.delete(verificationCode);
        }
        System.out.println("Found >> "+expiredOtpList.size()+" expired OTPs");
    }

    @Scheduled(cron = "0 0 0 * * ?")  // This runs every day at midnight
    public void remove24HoursLog() {
        infoLoggerRepository.deleteAll();
        apiKeyService.deleteApiKeyIfExpired();
    }
}
