package com.store.user.controller;

import com.store.user.error.BadRequestException;
import com.store.user.model.VerificationCode;
import com.store.user.response.VerificationCodeGrabber;
import com.store.user.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verificationCode")
public class VerificationCodeController {

    private final VerificationCodeService verificationCodeService;

    @Autowired
    public VerificationCodeController(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    @PostMapping("/getCode/{email}")
    public VerificationCode getVerificationCodeByEmail(@PathVariable String email) throws Exception {
        try {
            VerificationCode res = verificationCodeService.findVerificationCodesByEmail(email);
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
    public void saveVerificationCode(
            @RequestBody VerificationCodeGrabber verificationCodeGrabber
            ) throws Exception{
        try {
            verificationCodeService.saveVerificationCodesByEmail(verificationCodeGrabber);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorMessage("Can't decrypt the email");
            throw badRequestException;
        }
    }

}
