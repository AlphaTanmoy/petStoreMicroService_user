package com.store.user.repo;

import com.store.user.model.TireManagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TireRepository extends JpaRepository<TireManagement, String> {
    TireManagement findByTireCode(String tireCode);
    List<TireManagement> findByRankGreaterThanEqual(int rank);
}
