package com.store.user.collection;

import com.store.user.enums.DATA_STATUS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerJWTBlackList {
    private String actionTakenOn;
    private String actionTakenBy;
    private String comment;
    private DATA_STATUS dataStatus;
    private Instant createdDate;
}
