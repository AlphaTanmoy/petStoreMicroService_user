package com.store.user.model;

import com.store.user.enums.INFO_LOG_TYPE;
import com.store.user.model.superEntity.SuperEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "info_logger_customer")
public class CustomerInfoLogger extends SuperEntity {

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private INFO_LOG_TYPE type;

}
