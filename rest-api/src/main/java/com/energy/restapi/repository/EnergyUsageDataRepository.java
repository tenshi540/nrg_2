package com.energy.restapi.repository;

import com.energy.restapi.model.EnergyUsageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EnergyUsageDataRepository extends JpaRepository<EnergyUsageData, LocalDateTime> {
    List<EnergyUsageData> findByHourBetween(LocalDateTime start, LocalDateTime end);
}
