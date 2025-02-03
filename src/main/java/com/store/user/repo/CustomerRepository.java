package com.store.user.repo;

import com.store.user.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository  extends JpaRepository<Customer,String> {

    Customer findByEmail(String email);

    @Query(
            value = "SELECT COUNT(*) FROM customer c WHERE c.email = :email"
            , nativeQuery = true
    )
    Long countUserByEmail(@Param("email") String email);
}
