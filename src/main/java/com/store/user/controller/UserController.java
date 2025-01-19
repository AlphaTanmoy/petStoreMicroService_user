package com.store.user.controller;

import com.store.user.enums.USER_ROLE;
import com.store.user.request.RequestEmail;
import com.store.user.request.SignUpRequest;
import com.store.user.response.ApiResponse;
import com.store.user.response.AuthResponse;
import com.store.user.service.AuthService;
import com.store.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final AuthService authService;

    @PostMapping("/sent/otp")
    public ResponseEntity<ApiResponse> sentLoginOtp(
            @RequestBody RequestEmail req
    ) throws Exception {

        authService.sentLoginOtp(req);

        ApiResponse res = new ApiResponse();
        res.setStatus(true);
        res.setMessage("We will Send a OTP to your email-> "+req.getEmail());
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/createUser")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest req, HttpServletRequest httpRequest) throws Exception {

        String jwt=userService.createUser(req, httpRequest);

        AuthResponse res=new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Register Success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
        res.setStatus(true);
        return ResponseEntity.ok(res);
    }


}
