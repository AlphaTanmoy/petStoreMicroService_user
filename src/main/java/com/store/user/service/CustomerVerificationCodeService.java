package com.store.user.service;

import com.store.user.enums.RESPONSE_TYPE;
import com.store.user.error.BadRequestException;
import com.store.user.model.Customer;
import com.store.user.model.CustomerVerificationCode;
import com.store.user.repo.CustomerRepository;
import com.store.user.repo.CustomerVerificationCodeRepository;
import com.store.user.response.CustomerVerificationCodeGrabber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerVerificationCodeService {

    private CustomerVerificationCodeRepository CustomerVerificationCodeRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerVerificationCodeService(CustomerVerificationCodeRepository CustomerVerificationCodeRepository,CustomerRepository customerRepository) {
        this.CustomerVerificationCodeRepository = CustomerVerificationCodeRepository;
        this.customerRepository = customerRepository;
    }

    public RESPONSE_TYPE saveOtpFromAuthMicroService(String emailId){
        Customer findCustomer = customerRepository.findByEmail(emailId);
        List<CustomerVerificationCode> code = CustomerVerificationCodeRepository.findByEmail(emailId);

        if(!code.isEmpty() && findCustomer!=null){

            code.get(0).setCustomer(findCustomer);
            CustomerVerificationCodeRepository.save(code.get(0));
            return RESPONSE_TYPE.SUCCESS;
        }
        else return RESPONSE_TYPE.FAIL;
    }

    public CustomerVerificationCode findCustomerVerificationCodesByEmail(String email) throws BadRequestException {
        BadRequestException badRequestException = new BadRequestException();

        List<CustomerVerificationCode> codes = CustomerVerificationCodeRepository.findByEmail(email);

        if (codes.isEmpty()) {
            badRequestException.setErrorMessage("No OTPs found for Email Id: " + email);
            throw badRequestException;
        }
        return codes.get(0);
    }

    public void saveCustomerVerificationCodesByEmail(CustomerVerificationCodeGrabber CustomerVerificationCodeGrabber){
        BadRequestException badRequestException = new BadRequestException();

        List<CustomerVerificationCode> codes = CustomerVerificationCodeRepository.findByEmail(CustomerVerificationCodeGrabber.getEmail());
        if (codes.isEmpty()) {
            badRequestException.setErrorMessage("Can't Save for: " + CustomerVerificationCodeGrabber.getEmail());
            throw badRequestException;
        }

        codes.get(0).setCustomer(CustomerVerificationCodeGrabber.getCustomer());
        CustomerVerificationCodeRepository.save(codes.get(0));
    }

}
