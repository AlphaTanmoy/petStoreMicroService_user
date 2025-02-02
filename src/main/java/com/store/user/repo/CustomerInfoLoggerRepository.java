package com.store.user.repo;

import com.store.user.model.CustomerInfoLogger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerInfoLoggerRepository extends JpaRepository<CustomerInfoLogger,String> {
}
