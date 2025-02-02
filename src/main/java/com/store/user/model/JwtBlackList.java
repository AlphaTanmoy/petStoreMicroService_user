package com.store.user.model;

import com.store.user.enums.DATA_STATUS;
import com.store.user.model.superEntity.SuperEntityWithOutDataStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "jwt_black_list")
public class JwtBlackList extends SuperEntityWithOutDataStatus {

    @Column(nullable = false)
    private String actionTakenOn;

    @Column(nullable = false)
    private String actionTakenBy;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DATA_STATUS dataStatus = DATA_STATUS.INACTIVE;
}
