package com.store.user.service;

import com.store.user.enums.RESPONSE_TYPE;
import com.store.user.error.BadRequestException;
import com.store.user.model.Customer;
import com.store.user.model.VerificationCode;
import com.store.user.repo.CustomerRepository;
import com.store.user.repo.VerificationCodeRepository;
import com.store.user.response.VerificationCodeGrabber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private VerificationCodeRepository verificationCodeRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public VerificationCodeService(VerificationCodeRepository verificationCodeRepository,CustomerRepository customerRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.customerRepository = customerRepository;
    }

    public RESPONSE_TYPE saveOtpFromAuthMicroService(String emailId){
        Customer findCustomer = customerRepository.findByEmail(emailId);
        List<VerificationCode> code = verificationCodeRepository.findByEmail(emailId);

        if(!code.isEmpty() && findCustomer!=null){

            code.get(0).setCustomer(findCustomer);
            verificationCodeRepository.save(code.get(0));
            return RESPONSE_TYPE.SUCCESS;
        }
        else return RESPONSE_TYPE.FAIL;
    }

    public VerificationCode findVerificationCodesByEmail(String email) throws BadRequestException {
        BadRequestException badRequestException = new BadRequestException();

        List<VerificationCode> codes = verificationCodeRepository.findByEmail(email);

        if (codes.isEmpty()) {
            badRequestException.setErrorMessage("No OTPs found for Email Id: " + email);
            throw badRequestException;
        }
        return codes.get(0);
    }

    public void saveVerificationCodesByEmail(VerificationCodeGrabber verificationCodeGrabber){
        BadRequestException badRequestException = new BadRequestException();

        List<VerificationCode> codes = verificationCodeRepository.findByEmail(verificationCodeGrabber.getEmail());
        if (codes.isEmpty()) {
            badRequestException.setErrorMessage("Can't Save for: " + verificationCodeGrabber.getEmail());
            throw badRequestException;
        }

        codes.get(0).setCustomer(verificationCodeGrabber.getCustomer());
        verificationCodeRepository.save(codes.get(0));
    }

}
