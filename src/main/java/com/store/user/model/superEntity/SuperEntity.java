package com.store.user.model.superEntity;

import com.store.user.enums.DATA_STATUS;
import com.store.user.utils.GenerateUUID;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class SuperEntity {

    @Id
    private String id = GenerateUUID.generateShortUUID();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DATA_STATUS DATASTATUS = DATA_STATUS.ACTIVE;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();
}
