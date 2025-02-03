package com.store.user.service;

import com.store.user.error.BadRequestException;
import com.store.user.model.Customer;
import com.store.user.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public void CustomerRepository(CustomerRepository customerRepository) throws BadRequestException{
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> findCustomerById(String id) throws BadRequestException {
        return customerRepository.findById(id);
    }

    public Customer findCustomerByEmail(String email) throws BadRequestException{
        Customer findCustomer = customerRepository.findByEmail(email);
        BadRequestException badRequestException = new BadRequestException();
        badRequestException.setErrorMessage("Customer with " + email + " does not exist");
        if(findCustomer==null) throw badRequestException;
        else return findCustomer;
    }

    public void saveCustomer(Customer Customer){
        customerRepository.save(Customer);
    }
}
