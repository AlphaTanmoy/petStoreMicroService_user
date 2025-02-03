package com.store.user.service;

import com.store.user.enums.DATA_STATUS;
import com.store.user.enums.USER_ROLE;
import com.store.user.error.BadRequestException;
import com.store.user.model.Customer;
import com.store.user.model.JwtBlackListCustomer;
import com.store.user.repo.JWTBlackListRepositoryCustomer;
import com.store.user.request.JWTBlackListRequest;
import com.store.user.response.JWTBlackListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JWTBlackListService {

    private final JWTBlackListRepositoryCustomer jwtBlackListRepository;
    private final CustomerService customerService;


    public boolean findBlackListedUserById(String userId){
        Long findUser = jwtBlackListRepository.findByUserId(userId);
        return findUser != 0;
    }


    public JWTBlackListResponse jwtBlackListOperator(JWTBlackListRequest jwtBlackListRequest, String actionTakerId, Boolean addToJwtBlackList) throws  BadRequestException{

        JWTBlackListResponse jwtBlackListResponse = new JWTBlackListResponse();
        BadRequestException badRequestException = new BadRequestException();

        Optional<Customer> actionTaker = customerService.findCustomerById(actionTakerId);
        USER_ROLE adminOrMasterConfirmation = actionTaker.get().getRole();

        if(adminOrMasterConfirmation.toString().equals(USER_ROLE.ROLE_ADMIN.toString())
                || adminOrMasterConfirmation.toString().equals(USER_ROLE.ROLE_MASTER.toString())){
            badRequestException.setErrorMessage("Only Admin and Master Users can have this access! ");
        }

        Optional<Customer> foundUser = customerService.findCustomerById(jwtBlackListRequest.getActionTakenForId());
        if(foundUser.isEmpty()){
            badRequestException.setErrorMessage("User not found");
            throw badRequestException;
        }

        Optional<JwtBlackListCustomer> foundUserFormJwtBlackList = jwtBlackListRepository.findById(jwtBlackListRequest.getActionTakenForId());
        if(foundUserFormJwtBlackList.isPresent()) {
            badRequestException.setErrorMessage("User already in JWT Black List");
            throw badRequestException;
        }

        DATA_STATUS currentDataStatus = foundUser.get().getDATASTATUS();
        if(currentDataStatus== DATA_STATUS.INACTIVE){
            badRequestException.setErrorMessage("User is already inactive");
            throw badRequestException;
        }else{
            foundUser.get().setDATASTATUS(DATA_STATUS.INACTIVE);
            customerService.saveCustomer(foundUser.get());
        }

        if(addToJwtBlackList) {
            JwtBlackListCustomer jwtBlackList = new JwtBlackListCustomer();
            jwtBlackList.setActionTakenBy(actionTakerId);
            jwtBlackList.setComment(jwtBlackListRequest.getComment());
            jwtBlackList.setActionTakenOn(jwtBlackListRequest.getActionTakenForId());
            jwtBlackList.setDataStatus(DATA_STATUS.INACTIVE);
            jwtBlackListRepository.save(jwtBlackList);
            jwtBlackListResponse.setActionTakenOnUser(jwtBlackListRequest.getActionTakenForId());
            jwtBlackListResponse.setComment(jwtBlackListRequest.getComment());
            jwtBlackListResponse.setDataStatus(DATA_STATUS.INACTIVE);
        }
        else {
            jwtBlackListRepository.deleteById(jwtBlackListRequest.getActionTakenForId());
            jwtBlackListResponse.setActionTakenOnUser(jwtBlackListRequest.getActionTakenForId());
            jwtBlackListResponse.setComment(jwtBlackListRequest.getComment());
            jwtBlackListResponse.setDataStatus(DATA_STATUS.ACTIVE);
        }
        return jwtBlackListResponse;
    }
}
