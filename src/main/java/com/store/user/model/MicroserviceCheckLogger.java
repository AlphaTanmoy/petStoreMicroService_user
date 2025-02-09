package com.store.user.model;

import com.store.user.enums.MICROSERVICE;
import com.store.user.enums.STATUS;
import com.store.user.model.superEntity.SuperEntityWithOutDataStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "micro_service_check_logger")
public class MicroserviceCheckLogger extends SuperEntityWithOutDataStatus {
    MICROSERVICE microservice;
    STATUS status;
}
