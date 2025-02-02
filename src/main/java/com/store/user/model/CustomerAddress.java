package com.store.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.user.model.superEntity.SuperEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_address")
public class CustomerAddress extends SuperEntity {

	private String homeFlatNumber;

	private String locality;

    private String address;

    private String city;

    private String state;

    private String pinCode;

    private String mobile;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

}
