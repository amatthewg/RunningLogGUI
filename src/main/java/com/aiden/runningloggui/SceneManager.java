package com.aiden.runningloggui;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static Map<String, Scene> scenes = new HashMap<>();
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public static void switchToScene(String name) {
        primaryStage.setScene(scenes.get(name));
    }

}
