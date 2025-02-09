package com.store.user.model.superEntity;

import com.store.user.enums.DATA_STATUS;
import com.store.user.utils.GenerateUUID;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Data
@MappedSuperclass
public abstract class SuperEntity {

    @Id
    private String id = GenerateUUID.generateShortUUID();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DATA_STATUS dataStatus = DATA_STATUS.ACTIVE;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @Column(nullable = false)
    private ZonedDateTime lastUpdated = ZonedDateTime.now();
}
