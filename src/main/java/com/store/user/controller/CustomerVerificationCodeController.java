package com.store.user.controller;

import com.store.user.error.BadRequestException;
import com.store.user.model.CustomerVerificationCode;
import com.store.user.response.CustomerVerificationCodeGrabber;
import com.store.user.service.CustomerVerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/CustomerVerificationCode")
public class CustomerVerificationCodeController {

    private final CustomerVerificationCodeService CustomerVerificationCodeService;

    @Autowired
    public CustomerVerificationCodeController(CustomerVerificationCodeService CustomerVerificationCodeService) {
        this.CustomerVerificationCodeService = CustomerVerificationCodeService;
    }

    @PostMapping("/getCode/{email}")
    public CustomerVerificationCode getCustomerVerificationCodeByEmail(@PathVariable String email) throws Exception {
        try {
            CustomerVerificationCode res = CustomerVerificationCodeService.findCustomerVerificationCodesByEmail(email);
            return res;
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorMessage("Can't decrypt the email");
            throw badRequestException;
        }
    }

    @PostMapping("/save")
    public void saveCustomerVerificationCode(
            @RequestBody CustomerVerificationCodeGrabber CustomerVerificationCodeGrabber
            ) throws Exception{
        try {
            CustomerVerificationCodeService.saveCustomerVerificationCodesByEmail(CustomerVerificationCodeGrabber);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorMessage("Can't decrypt the email");
            throw badRequestException;
        }
    }

}
