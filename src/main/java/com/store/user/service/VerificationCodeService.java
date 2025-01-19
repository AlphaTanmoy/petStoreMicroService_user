package com.store.user.service;

import com.store.user.RestTemplateMaster;
import com.store.user.error.BadRequestException;
import com.store.user.response.VerificationCodeGrabber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

import static com.store.user.config.KeywordsAndConstants.AUTH_MICROSERVICE_BASE_URL_LOC;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    @Autowired
    private final RestTemplateMaster restTemplateMaster;

    public VerificationCodeGrabber getVerificationEntityResponse(String emailId){
        String urlWithParams = UriComponentsBuilder
                .fromUri(URI.create(AUTH_MICROSERVICE_BASE_URL_LOC + "/verificationCode/getCode/" + emailId))
                .toUriString();

        ResponseEntity<VerificationCodeGrabber> response = restTemplateMaster.getRestTemplate().exchange(
                urlWithParams,
                HttpMethod.POST,
                null,
                VerificationCodeGrabber.class
        );

        if(Objects.requireNonNull(response.getBody()).getOtp().isEmpty()) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorMessage("Inner Microservice Communication Failed to Execute");
            badRequestException.setErrorType("Failed to get Verification Code for -> "+emailId);
            throw badRequestException;
        }

        return response.getBody();
    }

    public void saveVerificationCode(VerificationCodeGrabber verificationCodeGrabber){
        String urlWithParams = UriComponentsBuilder
                .fromUri(URI.create(AUTH_MICROSERVICE_BASE_URL_LOC + "/verificationCode/save"))
                .toUriString();

        HttpEntity<VerificationCodeGrabber> requestEntity = new HttpEntity<>(verificationCodeGrabber);

        ResponseEntity<VerificationCodeGrabber> response = restTemplateMaster.getRestTemplate().exchange(
                urlWithParams,
                HttpMethod.POST,
                requestEntity,
                VerificationCodeGrabber.class
        );

        if(!response.getStatusCode().is2xxSuccessful()){
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorMessage("Inner Microservice Communication Failed to Execute");
            badRequestException.setErrorType("Failed to Save Verification Code");
        }
    }
}
