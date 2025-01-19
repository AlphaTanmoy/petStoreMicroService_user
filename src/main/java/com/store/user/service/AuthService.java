package com.store.user.service;

import com.store.user.RestTemplateMaster;
import com.store.user.error.BadRequestException;
import com.store.user.request.RequestEmail;
import com.store.user.response.AuthResponse;
import com.store.user.response.VerificationCodeGrabber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.store.user.config.KeywordsAndConstants.AUTH_MICROSERVICE_BASE_URL_LOC;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private final RestTemplateMaster restTemplateMaster;

    public AuthResponse sentLoginOtp(RequestEmail req) {
        String urlWithParams = UriComponentsBuilder
                .fromUri(URI.create(AUTH_MICROSERVICE_BASE_URL_LOC + "/sent/otp"))
                .toUriString();

        HttpEntity<RequestEmail> requestEntity = new HttpEntity<>(req);

        try {
            ResponseEntity<AuthResponse> response = restTemplateMaster.getRestTemplate().exchange(
                    urlWithParams,
                    HttpMethod.POST,
                    requestEntity,
                    AuthResponse.class
            );

            if (response.getBody() == null || response.getBody().getMessage() == null || response.getBody().getMessage().isEmpty()) {
                throw new BadRequestException("Inner Microservice Communication Failed to Execute");
            }

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new BadRequestException("Error communicating with auth microservice: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new BadRequestException("An unexpected error occurred while communicating with the auth microservice" + e);
        }
    }

}
