package com.energy.restapi.controller;

import com.energy.restapi.model.PercentageData;
import com.energy.restapi.model.EnergyUsageData;
import com.energy.restapi.repository.PercentageDataRepository;
import com.energy.restapi.repository.EnergyUsageDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/energy")
public class PercentageController {

    @Autowired
    private PercentageDataRepository percentageRepo;

    @Autowired
    private EnergyUsageDataRepository energyUsageRepo;

    // GET /energy/current → from current_percentage
    @GetMapping("/current")
    public PercentageData getCurrentPercentage() {
        return percentageRepo.findTopByOrderByHourDesc().orElse(null);
    }

    // GET /energy/historical?start=...&end=... → from energy_usage
    @GetMapping("/historical")
    public List<EnergyUsageData> getHistoricalData(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return energyUsageRepo.findByHourBetween(start, end);
    }
}
