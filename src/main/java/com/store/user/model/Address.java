package com.store.user.model;

import com.store.user.model.superEntity.SuperEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "address")
public class Address extends SuperEntity {

	private String name;
	private String locality;
	private String address;
    private String city;
    private String state;
    private String pinCode;

}
