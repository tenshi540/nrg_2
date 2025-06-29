package com.energy.restapi.repository;

import com.energy.restapi.model.PercentageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PercentageDataRepository extends JpaRepository<PercentageData, LocalDateTime> {
    Optional<PercentageData> findTopByOrderByHourDesc(); // latest row
}
