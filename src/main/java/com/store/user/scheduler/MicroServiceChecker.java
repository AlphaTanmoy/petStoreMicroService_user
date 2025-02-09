package com.store.user.scheduler;

import com.store.user.enums.MICROSERVICE;
import com.store.user.enums.STATUS;
import com.store.user.model.MicroserviceCheckLogger;
import com.store.user.repo.MicroserviceCheckLoggerRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

import static com.store.user.config.KeywordsAndConstants.*;

@Component
public class MicroServiceChecker {

    private final MicroserviceCheckLoggerRepository microserviceCheckLoggerRepository;

    public MicroServiceChecker(MicroserviceCheckLoggerRepository microserviceCheckLoggerRepository) {
        this.microserviceCheckLoggerRepository = microserviceCheckLoggerRepository;
    }

    public void checkMicroServices() {
        checkService(MICROSERVICE.ADMIN, ADMIN_MICROSERVICE_BASE_URL_LOC + "/preHitter");
        checkService(MICROSERVICE.AUTHENTICATION, AUTH_MICROSERVICE_BASE_URL_LOC + "/preHitter");
        checkService(MICROSERVICE.CHAT, CHAT_MICROSERVICE_BASE_URL_LOC + "/preHitter");
        checkService(MICROSERVICE.CORE, CORE_MICROSERVICE_BASE_URL_LOC + "/preHitter");
        checkService(MICROSERVICE.PAYMENT, PAYMENT_MICROSERVICE_BASE_URL_LOC + "/preHitter");
        checkService(MICROSERVICE.SELLER, SELLER_MICROSERVICE_BASE_URL_LOC + "/preHitter");
        checkService(MICROSERVICE.USER, USER_MICROSERVICE_BASE_URL_LOC + "/preHitter");
    }

    private void checkService(MICROSERVICE microservice, String serviceUrl) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(serviceUrl, HttpMethod.GET, entity, String.class);
            STATUS status = (response.getStatusCode() == HttpStatus.OK) ? STATUS.UP : STATUS.DOWN;

            updateMicroserviceStatus(microservice, status);
        } catch (Exception e) {
            updateMicroserviceStatus(microservice, STATUS.DOWN);
        }
    }

    private void updateMicroserviceStatus(MICROSERVICE microservice, STATUS status) {
        Optional<MicroserviceCheckLogger> existingMicroservice =
                microserviceCheckLoggerRepository.findByMicroserviceName(microservice.name());

        if (existingMicroservice.isPresent()) {
            existingMicroservice.get().setStatus(status);
            microserviceCheckLoggerRepository.save(existingMicroservice.get());
        } else {
            MicroserviceCheckLogger newLogger = new MicroserviceCheckLogger();
            newLogger.setMicroservice(microservice);
            newLogger.setStatus(status);
            microserviceCheckLoggerRepository.save(newLogger);
        }
    }
}
