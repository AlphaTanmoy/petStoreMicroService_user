package com.store.user.model.superEntity;

import com.store.user.enums.DATA_STATUS;
import com.store.user.utils.GenerateUUID;
import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class SuperEntityWithIdAndState {

    @Id
    private String id = GenerateUUID.generateShortUUID();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DATA_STATUS DATASTATUS = DATA_STATUS.ACTIVE;
}
