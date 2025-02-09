package com.store.user.controller;

import com.store.user.utils.JwtProvider;
import com.store.user.config.KeywordsAndConstants;
import com.store.user.error.BadRequestException;
import com.store.user.request.JWTBlackListRequest;
import com.store.user.response.JWTBlackListResponse;
import com.store.user.service.JWTBlackListService;
import com.store.user.utils.ValidateForUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping()
@RequiredArgsConstructor
public class JWTBlackListController {

    private final JWTBlackListService jwtBlackListService;
    private final JwtProvider jwtProvider;

    @PostMapping("/addToBlackList")
    public JWTBlackListResponse addToBlackList(
            @Valid @RequestHeader(value = KeywordsAndConstants.HEADER_AUTH_TOKEN, required = false)
            String token,
            @RequestBody JWTBlackListRequest jwtBlackListRequest
    ) {

        BadRequestException badRequestException = new BadRequestException();
        String actionTakerId;
        if (token != null) {
            actionTakerId = jwtProvider.getIdFromJwtToken(token);
        } else {
            badRequestException.setErrorMessage("Invalid token");
            throw badRequestException;
        }
        ValidateForUUID.check(actionTakerId, "Customer");
        ValidateForUUID.check(jwtBlackListRequest.getActionTakenForId(), "Customer");

        return jwtBlackListService.jwtBlackListOperator(jwtBlackListRequest, actionTakerId, true);

    }

    @PostMapping("/removeFromBlackList")
    public JWTBlackListResponse removeFromBlackList(
            @Valid @RequestHeader(value = KeywordsAndConstants.HEADER_AUTH_TOKEN, required = false)
            String token,
            @RequestBody JWTBlackListRequest jwtBlackListRequest
    ) {

        BadRequestException badRequestException = new BadRequestException();
        String actionTakerId;
        if (token != null) {
            actionTakerId = jwtProvider.getIdFromJwtToken(token);
        } else {
            badRequestException.setErrorMessage("Invalid token");
            throw badRequestException;
        }
        ValidateForUUID.check(actionTakerId, "Customer");
        ValidateForUUID.check(jwtBlackListRequest.getActionTakenForId(), "Customer");

        return jwtBlackListService.jwtBlackListOperator(jwtBlackListRequest, actionTakerId, false);

    }

    @PostMapping("/findBlackListedCustomer/{customerId}")
    public boolean isBlackListedUser(
            @PathVariable String customerId
    ){
        boolean isBlackListed = jwtBlackListService.findBlackListedUserById(customerId);
        return isBlackListed;
    }

}
