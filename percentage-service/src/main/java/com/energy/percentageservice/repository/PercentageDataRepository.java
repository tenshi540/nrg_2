package com.energy.percentageservice.repository;

import com.energy.percentageservice.model.PercentageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PercentageDataRepository extends JpaRepository<PercentageData, LocalDateTime> {
}
