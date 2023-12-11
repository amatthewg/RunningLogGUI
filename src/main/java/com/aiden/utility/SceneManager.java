package com.aiden.utility;

import com.aiden.misc.RunningLogGUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static Map<String, Scene> scenes = new HashMap<>();
    private static Stage primaryStage;



    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void showPrimaryStage() {
        primaryStage.show();
    }
    public static void loadScene(String fxmlPath, String sceneName){
        try {
            FXMLLoader loader = new FXMLLoader(RunningLogGUI.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scenes.put(sceneName, scene);
        } catch(Exception e) {
            // Scene failed to load
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to load resource");
            alert.setContentText("Failed to load scene '" + sceneName + "' located at " + fxmlPath);
            alert.showAndWait();
            e.printStackTrace();
            throw new RuntimeException("Resource not located: " + fxmlPath);
        }
    }

    public static void setScene(String name) {
        Scene scene = scenes.get(name);
        if(scene != null) {
            primaryStage.setScene(scene);
            // Update preferences with this scene
            PreferencesManager.put(AppConstants.LAST_SCENE_OPENED_KEY, name);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to find resource");
            alert.setContentText("Failed to get scene from scenes map: " + name);
            alert.showAndWait();
            throw new RuntimeException("Scene '" + name + "' was null in SceneManager scene map");
        }
    }

}
