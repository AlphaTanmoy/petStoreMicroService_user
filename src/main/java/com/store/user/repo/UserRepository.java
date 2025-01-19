package com.store.user.repo;

import com.store.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users,String> {

    Users findByEmail(String email);

    @Query(
            value = "SELECT COUNT(*) FROM users u WHERE u.email = :email"
            , nativeQuery = true
    )
    Long countUserByEmail(@Param("email") String email);
}
