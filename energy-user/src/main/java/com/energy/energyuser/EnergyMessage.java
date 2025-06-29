package com.energy.energyuser;

import java.time.LocalDateTime;

public class EnergyMessage {
    public String type;
    public String association;
    public double kwh;
    public LocalDateTime datetime;

    public EnergyMessage(String type, String association, double kwh, LocalDateTime datetime) {
        this.type = type;
        this.association = association;
        this.kwh = kwh;
        this.datetime = datetime;
    }

    // Needed for RabbitMQ to serialize/deserialize JSON
    public EnergyMessage() {}
}
