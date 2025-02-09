package com.store.user.scheduler;

import com.store.user.enums.MICROSERVICE;
import com.store.user.enums.STATUS;
import com.store.user.model.MicroserviceCheckLogger;
import com.store.user.repo.MicroserviceCheckLoggerRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.store.user.config.KeywordsAndConstants.*;

@Component
public class MicroServiceChecker {

    private final MicroserviceCheckLoggerRepository microserviceCheckLoggerRepository;

    public MicroServiceChecker(MicroserviceCheckLoggerRepository microserviceCheckLoggerRepository) {
        this.microserviceCheckLoggerRepository = microserviceCheckLoggerRepository;
    }

    void checkMicroServices(){
        checkAdmin();
        checkAuthentication();
        checkChat();
        checkCore();
        checkPayment();
        checkSeller();
        checkUser();
    }

    private void checkAdmin(){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String authServiceUrl = ADMIN_MICROSERVICE_BASE_URL_LOC + "/preHitter";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> authRequestBody = new HashMap<>();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(authRequestBody, headers);
            ResponseEntity<String> authResponse = restTemplate.postForEntity(authServiceUrl, entity, String.class);
            if (authResponse.getStatusCode() != HttpStatus.OK) {
               Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.ADMIN.name());
               if(adminMicroService.isPresent()){
                   adminMicroService.get().setStatus(STATUS.UP);
                   microserviceCheckLoggerRepository.save(adminMicroService.get());
               } else {
                   MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                   newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.ADMIN);
                   newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
               }
            }
        }catch (Exception e){
            Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.ADMIN.name());
            if(adminMicroService.isPresent()){
                adminMicroService.get().setStatus(STATUS.DOWN);
                microserviceCheckLoggerRepository.save(adminMicroService.get());
            } else {
                MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.ADMIN);
                newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
            }
        }
    }

    private void checkAuthentication(){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String authServiceUrl = AUTH_MICROSERVICE_BASE_URL_LOC + "/preHitter";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> authRequestBody = new HashMap<>();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(authRequestBody, headers);
            ResponseEntity<String> authResponse = restTemplate.postForEntity(authServiceUrl, entity, String.class);
            if (authResponse.getStatusCode() != HttpStatus.OK) {
                Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.AUTHENTICATION.name());
                if(adminMicroService.isPresent()){
                    adminMicroService.get().setStatus(STATUS.UP);
                    microserviceCheckLoggerRepository.save(adminMicroService.get());
                } else {
                    MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                    newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.AUTHENTICATION);
                    newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
                }
            }
        }catch (Exception e){
            Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.AUTHENTICATION.name());
            if(adminMicroService.isPresent()){
                adminMicroService.get().setStatus(STATUS.DOWN);
                microserviceCheckLoggerRepository.save(adminMicroService.get());
            } else {
                MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.AUTHENTICATION);
                newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
            }
        }
    }

    private void checkChat(){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String authServiceUrl = CHAT_MICROSERVICE_BASE_URL_LOC + "/preHitter";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> authRequestBody = new HashMap<>();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(authRequestBody, headers);
            ResponseEntity<String> authResponse = restTemplate.postForEntity(authServiceUrl, entity, String.class);
            if (authResponse.getStatusCode() != HttpStatus.OK) {
                Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.CHAT.name());
                if(adminMicroService.isPresent()){
                    adminMicroService.get().setStatus(STATUS.UP);
                    microserviceCheckLoggerRepository.save(adminMicroService.get());
                } else {
                    MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                    newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.CHAT);
                    newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
                }
            }
        }catch (Exception e){
            Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.CHAT.name());
            if(adminMicroService.isPresent()){
                adminMicroService.get().setStatus(STATUS.DOWN);
                microserviceCheckLoggerRepository.save(adminMicroService.get());
            } else {
                MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.CHAT);
                newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
            }
        }
    }

    private void checkCore(){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String authServiceUrl = CORE_MICROSERVICE_BASE_URL_LOC + "/preHitter";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> authRequestBody = new HashMap<>();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(authRequestBody, headers);
            ResponseEntity<String> authResponse = restTemplate.postForEntity(authServiceUrl, entity, String.class);
            if (authResponse.getStatusCode() != HttpStatus.OK) {
                Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.CORE.name());
                if(adminMicroService.isPresent()){
                    adminMicroService.get().setStatus(STATUS.UP);
                    microserviceCheckLoggerRepository.save(adminMicroService.get());
                } else {
                    MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                    newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.CORE);
                    newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
                }
            }
        }catch (Exception e){
            Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.CORE.name());
            if(adminMicroService.isPresent()){
                adminMicroService.get().setStatus(STATUS.DOWN);
                microserviceCheckLoggerRepository.save(adminMicroService.get());
            } else {
                MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.CORE);
                newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
            }
        }
    }

    private void checkPayment(){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String authServiceUrl = PAYMENT_MICROSERVICE_BASE_URL_LOC + "/preHitter";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> authRequestBody = new HashMap<>();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(authRequestBody, headers);
            ResponseEntity<String> authResponse = restTemplate.postForEntity(authServiceUrl, entity, String.class);
            if (authResponse.getStatusCode() != HttpStatus.OK) {
                Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.PAYMENT.name());
                if(adminMicroService.isPresent()){
                    adminMicroService.get().setStatus(STATUS.UP);
                    microserviceCheckLoggerRepository.save(adminMicroService.get());
                } else {
                    MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                    newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.PAYMENT);
                    newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
                }
            }
        }catch (Exception e){
            Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.PAYMENT.name());
            if(adminMicroService.isPresent()){
                adminMicroService.get().setStatus(STATUS.DOWN);
                microserviceCheckLoggerRepository.save(adminMicroService.get());
            } else {
                MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.PAYMENT);
                newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
            }
        }
    }

    private void checkSeller(){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String authServiceUrl = SELLER_MICROSERVICE_BASE_URL_LOC + "/preHitter";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> authRequestBody = new HashMap<>();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(authRequestBody, headers);
            ResponseEntity<String> authResponse = restTemplate.postForEntity(authServiceUrl, entity, String.class);
            if (authResponse.getStatusCode() != HttpStatus.OK) {
                Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.SELLER.name());
                if(adminMicroService.isPresent()){
                    adminMicroService.get().setStatus(STATUS.UP);
                    microserviceCheckLoggerRepository.save(adminMicroService.get());
                } else {
                    MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                    newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.SELLER);
                    newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
                }
            }
        }catch (Exception e){
            Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.SELLER.name());
            if(adminMicroService.isPresent()){
                adminMicroService.get().setStatus(STATUS.DOWN);
                microserviceCheckLoggerRepository.save(adminMicroService.get());
            } else {
                MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.SELLER);
                newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
            }
        }
    }

    private void checkUser(){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String authServiceUrl = USER_MICROSERVICE_BASE_URL_LOC + "/preHitter";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> authRequestBody = new HashMap<>();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(authRequestBody, headers);
            ResponseEntity<String> authResponse = restTemplate.postForEntity(authServiceUrl, entity, String.class);
            if (authResponse.getStatusCode() != HttpStatus.OK) {
                Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.USER.name());
                if(adminMicroService.isPresent()){
                    adminMicroService.get().setStatus(STATUS.UP);
                    microserviceCheckLoggerRepository.save(adminMicroService.get());
                } else {
                    MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                    newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.USER);
                    newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
                }
            }
        }catch (Exception e){
            Optional<MicroserviceCheckLogger> adminMicroService = microserviceCheckLoggerRepository.findByMicroserviceName(MICROSERVICE.USER.name());
            if(adminMicroService.isPresent()){
                adminMicroService.get().setStatus(STATUS.DOWN);
                microserviceCheckLoggerRepository.save(adminMicroService.get());
            } else {
                MicroserviceCheckLogger newMicroServiceCheckLogger = new MicroserviceCheckLogger();
                newMicroServiceCheckLogger.setMicroservice(MICROSERVICE.USER);
                newMicroServiceCheckLogger.setStatus(STATUS.DOWN);
            }
        }
    }

}
