package com.store.user.repo;

import com.store.user.model.InfoLogger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoLoggerRepository extends JpaRepository<InfoLogger,String> {
}
