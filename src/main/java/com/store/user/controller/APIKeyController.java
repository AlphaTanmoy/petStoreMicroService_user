package com.store.user.controller;

import com.store.user.config.JwtProvider;
import com.store.user.config.KeywordsAndConstants;
import com.store.user.error.BadRequestException;
import com.store.user.request.ApiKeyGenerationRequest;
import com.store.user.response.ApiKeyResponse;
import com.store.user.service.ApiKeyService;
import com.store.user.utils.DateUtil;
import com.store.user.utils.ValidateForUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiKey")
@RequiredArgsConstructor
public class APIKeyController {

    private final ApiKeyService apiKeyService;
    private final JwtProvider jwtProvider;
    private final DateUtil dateUtil;
    @PostMapping("/createApiKey")
    public ApiKeyResponse createApiKey(
            @Valid @RequestHeader(value = KeywordsAndConstants.HEADER_AUTH_TOKEN, required = false) String token,
            @RequestBody ApiKeyGenerationRequest apiKeyGenerationRequest
    ){
        BadRequestException badRequestException = new BadRequestException();
        String actionTakerId;
        if (token != null) {
            actionTakerId = jwtProvider.getIdFromJwtToken(token);
        } else {
            badRequestException.setErrorMessage("Invalid token");
            throw badRequestException;
        }
        ValidateForUUID.check(actionTakerId, "User");
        ValidateForUUID.check(apiKeyGenerationRequest.getId(), "User");

        return apiKeyService.createApiKey(
                actionTakerId,apiKeyGenerationRequest.getId(),
                apiKeyGenerationRequest.getExpiryDate(),
                DateUtil.checkValid(apiKeyGenerationRequest)
        );
    }

    @PostMapping("/deleteApiKey")
    public ApiKeyResponse deleteApiKey(
            @Valid @RequestHeader(value = KeywordsAndConstants.HEADER_AUTH_TOKEN, required = false) String token,
            @RequestBody String id
    ){
        BadRequestException badRequestException = new BadRequestException();
        String actionTakerId;
        if (token != null) {
            actionTakerId = jwtProvider.getIdFromJwtToken(token);
        } else {
            badRequestException.setErrorMessage("Invalid token");
            throw badRequestException;
        }
        ValidateForUUID.check(actionTakerId, "User");
        ValidateForUUID.check(id, "User");
        return apiKeyService.deleteApiKey(actionTakerId,id);
    }

}
