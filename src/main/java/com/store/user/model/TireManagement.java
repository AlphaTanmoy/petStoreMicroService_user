package com.store.user.model;

import com.store.user.enums.TIRE_CODE;
import com.store.user.model.superEntity.SuperEntityWithoutExpiry;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "tire")
@EqualsAndHashCode(callSuper = true)
public class TireManagement extends SuperEntityWithoutExpiry {

    @NotNull
    @Column(name = "api_access_path", nullable = false)
    private String apiAccessPath;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TIRE_CODE tireCode;

    @NotNull
    @Column(name = "rank", nullable = false)
    private int rank;
}
