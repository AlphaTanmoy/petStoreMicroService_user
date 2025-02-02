package com.store.user.response;

import com.store.user.enums.TIRE_CODE;
import com.store.user.enums.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProfile {

    private String name;
    private String apiKey;
    private USER_ROLE userRole;
    private TIRE_CODE tireCode;
}
