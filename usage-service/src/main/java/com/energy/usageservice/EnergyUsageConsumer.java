package com.energy.usageservice;

import com.energy.usageservice.model.EnergyMessage;
import com.energy.usageservice.service.EnergyUsageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EnergyUsageConsumer {

    private final EnergyUsageService usageService;

    public EnergyUsageConsumer(EnergyUsageService usageService) {
        this.usageService = usageService;
    }

    @RabbitListener(queues = "${energy.queue}")
    public void receiveMessage(EnergyMessage message) {
        System.out.println("Received energy message: ");
        System.out.println("  Type: " + message.getType());
        System.out.println("  Association: " + message.getAssociation());
        System.out.println("  kWh: " + message.getKwh());
        System.out.println("  Datetime: " + message.getDatetime());

        usageService.processMessage(message);
    }
}
