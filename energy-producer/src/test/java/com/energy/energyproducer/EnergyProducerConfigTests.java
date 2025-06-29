package com.energy.energyproducer;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EnergyProducerConfigTests {

    @Autowired
    private Queue queue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void queueBeanExistsAndIsCorrectlyNamed() {
        assertNotNull(queue, "Queue bean should be present in context");
        //Prüft, dass eine Queue-Bean erstellt und auch injiziert wird
        assertEquals("energy.queue", queue.getName(),
                "Queue should be named 'energy.queue'");
        // Prüft den Namen der Queue; damit sichergestellt wird ob man sich nicht vertippt hat
    }

    @Test
    void rabbitTemplateUsesJacksonConverter() {
        assertNotNull(rabbitTemplate, "RabbitTemplate should be in the context");
        // prüft, ob RabbitTemplate Bean drinnen ist
        assertTrue(rabbitTemplate.getMessageConverter() instanceof Jackson2JsonMessageConverter,
                "RabbitTemplate must be configured with Jackson2JsonMessageConverter");
        //holt MessageConverter aus dem RabbitTemplate und prüft ob es der Jackson2JsonMessageConverter ist
        //Um sicherzustellen, dass unser Template Nachrichten automatisch in JSON um und wieder zurück gewandelt wird
    }
}


