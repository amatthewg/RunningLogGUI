package com.aiden.runningloggui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class RunningLogGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL resourceURL = getClass().getResource("configure-storage-settings.fxml");
        Scene scene = new Scene(loader.load(resourceURL));
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private static void loadFxmlFiles() {

    }


}
