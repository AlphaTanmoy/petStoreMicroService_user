package com.store.user.request;

import com.store.user.enums.DATE_RANGE_TYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyGenerationRequest {
    private String id;
    private DATE_RANGE_TYPE expiryDate;
}
