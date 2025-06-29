package com.energy.jfxgui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GUIController {

    public VBox createLayout() {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        // CURRENT SECTION
        Text currentCommunityPool = new Text("Community Pool: -");
        Text currentGridPortion = new Text("Grid Portion: -");
        Button refreshButton = new Button("Refresh");

        refreshButton.setOnAction(e -> fetchCurrent(currentCommunityPool, currentGridPortion));

        VBox currentBox = new VBox(5, currentCommunityPool, currentGridPortion, refreshButton);

        // HISTORICAL SECTION
        DatePicker startPicker = new DatePicker(LocalDate.now().minusDays(1));
        DatePicker endPicker = new DatePicker(LocalDate.now());

        Spinner<Integer> startHourSpinner = new Spinner<>(0, 23, 0);
        Spinner<Integer> endHourSpinner = new Spinner<>(0, 23, 23);
        startHourSpinner.setEditable(true);
        endHourSpinner.setEditable(true);

        Button showButton = new Button("Show Data");

        Text totalProduced = new Text("Community produced: -");
        Text totalUsed = new Text("Community used: -");
        Text totalGrid = new Text("Grid used: -");

        showButton.setOnAction(e -> {
            LocalDate startDate = startPicker.getValue();
            LocalDate endDate = endPicker.getValue();
            int startHour = startHourSpinner.getValue();
            int endHour = endHourSpinner.getValue();

            LocalDateTime start = startDate.atTime(startHour, 0);
            LocalDateTime end = endDate.atTime(endHour, 59);

            fetchHistorical(start, end, totalProduced, totalUsed, totalGrid);
        });

        VBox historicalBox = new VBox(5,
                new HBox(10,
                        new Label("Start:"), startPicker,
                        new Label("Hour:"), startHourSpinner,
                        new Label("End:"), endPicker,
                        new Label("Hour:"), endHourSpinner
                ),
                showButton,
                totalProduced,
                totalUsed,
                totalGrid
        );

        root.getChildren().addAll(currentBox, new Separator(), historicalBox);
        return root;
    }

    private void fetchCurrent(Text poolText, Text gridText) {
        new Thread(() -> {
            try {
                URL url = new URL("http://localhost:8084/energy/current");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Scanner scanner = new Scanner(conn.getInputStream());
                String json = scanner.useDelimiter("\\A").next();
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(json);

                double communityDepleted = node.get("communityDepleted").asDouble();
                double gridPortion = node.get("gridPortion").asDouble();

                Platform.runLater(() -> {
                    poolText.setText(String.format("Community Pool: %.2f%% used", communityDepleted));
                    gridText.setText(String.format("Grid Portion: %.2f%%", gridPortion * 100));
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    poolText.setText("Error loading current data.");
                    gridText.setText("");
                });
                e.printStackTrace();
            }
        }).start();
    }

    private void fetchHistorical(LocalDateTime start, LocalDateTime end,
                                 Text producedText, Text usedText, Text gridText) {

        new Thread(() -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                String urlStr = String.format("http://localhost:8084/energy/historical?start=%s&end=%s",
                        start.format(formatter), end.format(formatter));

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Scanner scanner = new Scanner(conn.getInputStream());
                String json = scanner.useDelimiter("\\A").next();
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode array = mapper.readTree(json);

                double produced = 0, used = 0, grid = 0;
                for (JsonNode node : array) {
                    produced += node.get("communityProduced").asDouble();
                    used += node.get("communityUsed").asDouble();
                    grid += node.get("gridUsed").asDouble();
                }

                double finalProduced = produced;
                double finalUsed = used;
                double finalGrid = grid;

                Platform.runLater(() -> {
                    producedText.setText(String.format("Community produced: %.3f kWh", finalProduced));
                    usedText.setText(String.format("Community used: %.3f kWh", finalUsed));
                    gridText.setText(String.format("Grid used: %.3f kWh", finalGrid));
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    producedText.setText("Error loading historical data.");
                    usedText.setText("");
                    gridText.setText("");
                });
                e.printStackTrace();
            }
        }).start();
    }
}
