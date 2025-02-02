package com.store.user.service;

import com.store.user.customDTO.TwoParameterDTO;
import com.store.user.enums.CREATION_STATUS;
import com.store.user.enums.DATE_RANGE_TYPE;
import com.store.user.error.BadRequestException;
import com.store.user.model.ApiKey;
import com.store.user.repo.APIKeyRepository;
import com.store.user.response.ApiKeyResponse;
import com.store.user.utils.GenerateUUID;
import com.store.user.utils.ValidateTire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final APIKeyRepository apiKeyRepository;
    private final ValidateTire validateTire;

    public ApiKeyResponse createApiKey(String actionTakerId, String id, DATE_RANGE_TYPE expiry, TwoParameterDTO twoParameterDTO) throws BadRequestException{
        boolean isDateRangeTypeValid = (boolean) twoParameterDTO.getFirstParameter();
        boolean checkIfHaveAuthority = ValidateTire.hierarchyTireManagement(actionTakerId,id);
        if(isDateRangeTypeValid && checkIfHaveAuthority){
            ZonedDateTime timeToExpire = ZonedDateTime.now().plusDays((Long) twoParameterDTO.getSecondParameter());
            String apiKey = GenerateUUID.generateAPIKey();
            ApiKey newEntry = new ApiKey();
            newEntry.setCreatedByUser(actionTakerId);
            newEntry.setCreatedForUser(id);
            newEntry.setApiKey(apiKey);
            newEntry.setExpiryDateForApiKey(expiry);
            newEntry.setTimeToExpire(timeToExpire);
            apiKeyRepository.save(newEntry);
            return new ApiKeyResponse(
                    actionTakerId,
                    id,
                    apiKey,
                    CREATION_STATUS.CREATED,
                    timeToExpire.toString()
            );
        }
        else{
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorMessage("Not a valid Request");
            throw badRequestException;
        }
    }

    public ApiKeyResponse deleteApiKey(String actionTakerId, String id) throws BadRequestException {
        ApiKey findApiKey = apiKeyRepository.findByCreatedForId(id);
        apiKeyRepository.delete(findApiKey);
        return new ApiKeyResponse(
                actionTakerId,
                id,
                "",
                CREATION_STATUS.DELETED,
                ""
        );
    }

    public String deleteApiKeyIfExpired() throws BadRequestException {
        List<ApiKey> totalApiKeyDeleted = apiKeyRepository.deleteApiKeyIfExpired();

        StringBuilder constructOutputString = new StringBuilder();

        if(totalApiKeyDeleted.isEmpty()) return constructOutputString.append("No Api Keys Expired, Delete 0 From Table. ").toString();

        constructOutputString.append("Deleted total Api Keys>> ").append(totalApiKeyDeleted.size()).append("\n");
        for (ApiKey apiKey : totalApiKeyDeleted) {
            constructOutputString.append("id>>").append(apiKey).append("\n");
        }

        return constructOutputString.toString();
    }

    public String findApiKeyByUserId(String id) throws BadRequestException {
        List<ApiKey> findApiKeyEntity = apiKeyRepository.findByUserId(id);
        if(findApiKeyEntity.isEmpty()){
            return null;
        }
        else return findApiKeyEntity.get(0).getApiKey();
    }

}
