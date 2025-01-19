package com.store.user.service;

import com.store.user.RestTemplateMaster;
import com.store.user.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

import static com.store.user.config.KeywordsAndConstants.AUTH_MICROSERVICE_BASE_URL_LOC;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Autowired
    private final RestTemplateMaster restTemplateMaster;

    public boolean isListedInJWTTable(String emailId) {
        String urlWithParams = UriComponentsBuilder
                .fromUri(URI.create(AUTH_MICROSERVICE_BASE_URL_LOC + "/findBlackListedUser/" + emailId))
                .toUriString();

        ResponseEntity<String> response = restTemplateMaster.getRestTemplate().exchange(
                urlWithParams,
                HttpMethod.POST,
                null,
                String.class
        );
        if(response.getBody().isEmpty()) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorMessage("Inner Microservice Communication Failed to Execute");
            throw badRequestException;
        }

        return Objects.equals(response.getBody(), "true");
    }

}
