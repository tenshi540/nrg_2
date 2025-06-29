package com.energy.usageservice;

import com.energy.usageservice.model.EnergyMessage;
import com.energy.usageservice.service.EnergyUsageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@SpringBootTest(
        // Verhindert, dass der echte RabbitListener beim Context-Start Nachrichten erwartet
        properties = "spring.rabbitmq.listener.simple.auto-startup=false"
)
class EnergyUsageConsumerSpringTest {

    @Autowired
    private EnergyUsageConsumer consumer;
    // Spring setzt hier den echten EnergyUsageConsumer ein (das Component, was die Rabbit-nachrichten entgegennimmt)

    @MockitoBean
    //Ersetzt den echten EnergyUsageService Bean durch ein Mockito-Mock (ein Fake).
    private EnergyUsageService usageService;

    private EnergyMessage sampleMessage;

    @BeforeEach
        // die Methode wird vor jedem einzelnen Test ausgeführt
        // es erstellst hier eine neue EnergyMessage mit fixierten Testwerten
    void setUp() {
        sampleMessage = new EnergyMessage(
                "PRODUCER",
                "COMMUNITY",
                1.23,
                LocalDateTime.of(2025, 6, 28, 12, 0)
        );
        //Testnachricht
    }

    @Test
    void receiveMessage_callsProcessMessage() {
        consumer.receiveMessage(sampleMessage);
        // simuliert, das RabbitMQ eine Nachricht liefert
        // welches normalerweise Spring via @RabbitListener tut

        verify(usageService).processMessage(sampleMessage);
        // Prüft, das EnergyUsageConsumer beim Eintreffen der Nachricht tatsächlich usageService.processMessage(...) aufruft mit der Testnachricht

    }
}