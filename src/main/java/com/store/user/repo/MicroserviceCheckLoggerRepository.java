package com.store.user.repo;

import com.store.user.model.MicroserviceCheckLogger;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MicroserviceCheckLoggerRepository extends JpaRepository<MicroserviceCheckLogger,String> {

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM micro_service_check_logger " +
                    "WHERE microservice = :microserviceName "
            , nativeQuery = true
    )
    void deleteByMicroserviceName(@Param("microserviceName") String microserviceName);

    @Query(
            value = "SELECT * FROM micro_service_check_logger " +
                    "WHERE microservice = :microserviceName "
            , nativeQuery = true
    )
    Optional<MicroserviceCheckLogger> findByMicroserviceName(@Param("microserviceName") String microserviceName);

}
