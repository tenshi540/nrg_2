package com.energy.energyproducer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

@SpringBootApplication
@EnableScheduling
public class EnergyProducerApplication implements CommandLineRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final Random random = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    private final String weatherApiKey = "API_KEY"; // Replace with your free OpenWeatherMap key

    public static void main(String[] args) {
        SpringApplication.run(EnergyProducerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Energy Producer running...");
    }

    @Scheduled(fixedRate = 5000) // Every 5 seconds
    public void sendEnergyUsage() {
        double kwh = 0.002 + (0.006 - 0.002) * random.nextDouble();

        String weather = fetchCurrentWeatherCondition();
        System.out.println("Weather in Edinburgh: " + weather);

        // Adjust production based on weather
        switch (weather) {
            case "Clear" -> kwh *= 1.5;
            case "Rain" -> kwh *= 0.7;
            default -> {} // "Clouds", "Fog", etc. = unchanged
        }

        Map<String, Object> message = new HashMap<>();
        message.put("type", "PRODUCER");
        message.put("association", "COMMUNITY");
        message.put("kwh", kwh);
        message.put("datetime", LocalDateTime.now());

        System.out.println("Sending energy usage: " + kwh);
        rabbitTemplate.convertAndSend("energy.queue", message);
    }

    private String fetchCurrentWeatherCondition() {
        try {
            String urlString = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?lat=55.9533&lon=-3.1883&appid=%s&units=metric",
                    weatherApiKey
            );

            System.out.println("Fetching weather from: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            String json = scanner.useDelimiter("\\A").next();
            scanner.close();

            JsonNode root = mapper.readTree(json);
            return root.get("weather").get(0).get("main").asText(); // e.g., "Clear", "Clouds", "Rain"

        } catch (Exception e) {
            System.err.println("Weather API request failed: " + e.getMessage());
            return "Unknown";
        }
    }
}
