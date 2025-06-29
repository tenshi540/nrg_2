package com.energy.jfxgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        GUIController controller = new GUIController();
        Scene scene = new Scene(controller.createLayout(), 500, 400);

        primaryStage.setTitle("Energy Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
