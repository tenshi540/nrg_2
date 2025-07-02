# NRG-Disys: Energy Consumption Monitoring System

A distributed system project built with **Spring Boot** and **JavaFX**, designed to monitor and visualize community energy usage. The architecture consists of six independent services communicating via **RabbitMQ** and storing data in a **PostgreSQL** database.

---

## 🔧 Components

1. **Energy Producer and User**  
   Simulates and sends energy consumption data (e.g., in kWh) to RabbitMQ.

2. **Usage Service**  
   Receives and stores raw energy usage data into PostgreSQL.

3. **Current Percentage Service**  
   Calculates the current hour’s energy split between community-depleted and grid-supplied power, and stores the percentages in a separate table.

4. **REST API (Spring Boot)**  
   Provides endpoints for:
   - `GET /energy/current` — current hour’s percentages  
   - `GET /energy/historical?start=...&end=...` — historical percentage data over a date/time range

5. **JavaFX GUI**  
   A desktop client that visualizes both current and historical energy percentage values via the REST API.

6. **Weather API Integration (Planned)**  
   Optional component for factoring weather data into energy behavior (e.g., solar input).

---

## 🛠️ Technologies Used

- Java 21+
- Spring Boot
- JavaFX
- RabbitMQ
- PostgreSQL
- Maven

---

## 🚀 How to Run

1. **Start RabbitMQ and PostgreSQL in Docker**
2. **Run each Spring Boot service individually** (can be run via IDE or command line):
   - Usage Service
   - Current Percentage Service
   - REST API
3. **Run the Energy Producer / Energy User** to simulate consumption.
4. **Launch the JavaFX GUI** to interact with the API and visualize data.
