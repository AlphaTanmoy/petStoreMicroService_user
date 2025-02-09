package com.store.user.model;

import com.store.user.enums.MICROSERVICE;
import com.store.user.enums.STATUS;
import com.store.user.model.superEntity.SuperEntityWithOutDataStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "micro_service_check_logger")
public class MicroserviceCheckLogger extends SuperEntityWithOutDataStatus {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    MICROSERVICE microservice;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    STATUS status;
}
