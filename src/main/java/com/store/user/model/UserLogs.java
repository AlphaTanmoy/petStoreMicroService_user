package com.store.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.store.user.model.superEntity.SuperEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "user_logs")
public class UserLogs extends SuperEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false, unique = true)
    private String deviceId;

    @Column(nullable = false, length = 2000)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jwtToken;

    private String deviceType;
    private String operatingSystem;
}
