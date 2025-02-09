package com.store.user.preHitter;

import com.store.user.enums.MICROSERVICE;
import com.store.user.enums.RESPONSE_TYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreHitterResponse {
    RESPONSE_TYPE responseType;
    MICROSERVICE microservice;
}
