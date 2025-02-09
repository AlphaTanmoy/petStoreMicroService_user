package com.store.user.controller;

import com.store.user.utils.JwtProvider;
import com.store.user.config.KeywordsAndConstants;
import com.store.user.enums.MICROSERVICE;
import com.store.user.enums.RESPONSE_TYPE;
import com.store.user.enums.USER_ROLE;
import com.store.user.error.BadRequestException;
import com.store.user.model.Customer;
import com.store.user.request.LoginRequest;
import com.store.user.request.RequestEmail;
import com.store.user.request.SignUpRequest;
import com.store.user.response.ApiResponse;
import com.store.user.response.AuthResponse;
import com.store.user.response.GetProfile;
import com.store.user.service.CustomerAuthService;
import com.store.user.service.CustomerService;
import com.store.user.service.CustomerVerificationCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AuthController {

    private final CustomerAuthService authService;
    private final JwtProvider jwtProvider;
    private final CustomerService customerService;
    private final CustomerVerificationCodeService customerVerificationCodeService;

    @PostMapping("/saveOtp/{emailId}")
    public String SaveOtp(@PathVariable String emailId){
        RESPONSE_TYPE response = customerVerificationCodeService.saveOtpFromAuthMicroService(emailId);
        if(response==RESPONSE_TYPE.FAIL) return "fail";
        else return "success";
    }

    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> createCustomerHandler(@RequestBody SignUpRequest req, HttpServletRequest httpRequest) throws BadRequestException {

        String jwt=authService.createCustomer(req, httpRequest, MICROSERVICE.USER);

        AuthResponse res=new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Register Success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
        res.setStatus(true);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/sent/otp")
    public ResponseEntity<ApiResponse> sentLoginOtp(
            @RequestBody RequestEmail req
    ) throws Exception {

        authService.sentLoginOtp(req.getEmail());

        ApiResponse res = new ApiResponse();
        res.setStatus(true);
        res.setMessage("We will send an OTP to your email -> " + req.getEmail());
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }


    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest, HttpServletRequest httpRequest) throws Exception {
        AuthResponse authResponse = authService.signIn(loginRequest, httpRequest);
        authResponse.setStatus(true);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/getProfile")
    public GetProfile getProfile(
            @Valid @RequestHeader(value = KeywordsAndConstants.HEADER_AUTH_TOKEN, required = false) String token
    ){
        BadRequestException badRequestException = new BadRequestException();
        if(token.isEmpty()) {
            badRequestException.setErrorMessage("Provide a valid Token");
        }

        String actionTakerId = jwtProvider.getIdFromJwtToken(token);
        Optional<Customer> findCustomer = customerService.findCustomerById(actionTakerId);
        GetProfile getProfile = new GetProfile();

        if(findCustomer.isPresent()){
            getProfile.setName(findCustomer.get().getFullName());
            getProfile.setUserRole(findCustomer.get().getRole());
            getProfile.setTireCode(findCustomer.get().getTireCode());
        }
        else{
            badRequestException.setErrorMessage("No User Data Found");
            throw badRequestException;
        }

        return getProfile;
    }
}
