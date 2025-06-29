package com.energy.energyuser;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
public class EnergyUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnergyUserApplication.class, args);
    }

    @Bean
    CommandLineRunner sendMessages(RabbitTemplate rabbitTemplate) {
        return args -> {
            Random rand = new Random();

            while (true) {
                LocalDateTime now = LocalDateTime.now();
                int hour = now.getHour();

                double baseKwh = 0.002 + (0.006 - 0.002) * rand.nextDouble();
                double multiplier = (hour >= 7 && hour < 9) || (hour >= 19 && hour < 21) ? 1.5 : 1.0;
                double adjustedKwh = baseKwh * multiplier;

                Map<String, Object> message = new HashMap<>();
                message.put("type", "USER");
                message.put("association", "COMMUNITY");
                message.put("kwh", adjustedKwh);
                message.put("datetime", now);

                rabbitTemplate.convertAndSend("energy.queue", message);
                System.out.println("Sent: " + adjustedKwh + " kWh (multiplier " + multiplier + ")");

                Thread.sleep(5000); // every 5 sec
            }
        };
    }

}
