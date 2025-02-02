package com.store.user.response;

import com.store.user.enums.DATA_STATUS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTBlackListResponse {
    private String actionTakenOnUser;
    private DATA_STATUS dataStatus;
    private String comment;
}
