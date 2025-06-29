package com.energy.usageservice.service;

import com.energy.usageservice.model.EnergyUsage;
import com.energy.usageservice.model.EnergyMessage;
import com.energy.usageservice.repository.EnergyUsageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class EnergyUsageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.update.queue}")
    private String updateQueue;


    private final EnergyUsageRepository repository;

    public EnergyUsageService(EnergyUsageRepository repository) {
        this.repository = repository;
    }

    public void processMessage(EnergyMessage message) {
        // Round timestamp to the full hour
        LocalDateTime hour = message.getDatetime().truncatedTo(ChronoUnit.HOURS);

        // Try to find existing record
        EnergyUsage usage = repository.findById(hour).orElseGet(() -> {
            EnergyUsage newUsage = new EnergyUsage();
            newUsage.setHour(hour);
            newUsage.setCommunityProduced(0.0);
            newUsage.setCommunityUsed(0.0);
            newUsage.setGridUsed(0.0);
            return newUsage;
        });

        double kwh = message.getKwh();

        if ("PRODUCER".equalsIgnoreCase(message.getType())) {
            usage.setCommunityProduced(usage.getCommunityProduced() + kwh);
        } else if ("USER".equalsIgnoreCase(message.getType())) {
            usage.setCommunityUsed(usage.getCommunityUsed() + kwh);

            // If user used more than was produced > draw from grid
            if (usage.getCommunityUsed() > usage.getCommunityProduced()) {
                double deficit = usage.getCommunityUsed() - usage.getCommunityProduced();
                usage.setGridUsed(usage.getGridUsed() + deficit);
            }
        }

        // Save or update in database
        repository.save(usage);

        rabbitTemplate.convertAndSend(updateQueue, usage);
    }
}
