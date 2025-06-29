package com.energy.percentageservice.service;

import com.energy.percentageservice.model.PercentageData;
import com.energy.percentageservice.model.PercentageMessage;
import com.energy.percentageservice.repository.PercentageDataRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PercentageConsumer {

    private final PercentageDataRepository repository;

    public PercentageConsumer(PercentageDataRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${rabbitmq.update.queue}")
    public void handleUpdate(PercentageMessage message) {
        List<Integer> hourParts = message.getHour();
        LocalDateTime hour = LocalDateTime.of(
                hourParts.get(0), hourParts.get(1), hourParts.get(2),
                hourParts.get(3), hourParts.get(4)
        );

        double produced = message.getCommunityProduced();
        double used = message.getCommunityUsed();
        double grid = message.getGridUsed();

        double communityDepleted = produced == 0 ? 100.0 : Math.min((used / produced) * 100.0, 100.0);
        double gridPortion = (grid / (produced + grid)) * 100.0;

        PercentageData data = new PercentageData();
        data.setHour(hour);
        data.setCommunityDepleted(communityDepleted);
        data.setGridPortion(gridPortion);

        repository.save(data);
    }
}
