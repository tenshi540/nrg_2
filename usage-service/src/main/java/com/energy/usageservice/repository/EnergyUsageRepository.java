package com.energy.usageservice.repository;
import com.energy.usageservice.model.EnergyUsage;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface EnergyUsageRepository extends JpaRepository<EnergyUsage, LocalDateTime> {
}
