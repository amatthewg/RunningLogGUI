package com.aiden.misc;

import com.aiden.utility.AppConstants;
import com.aiden.utility.PreferencesManager;
import com.aiden.utility.SaveFileManager;
import com.aiden.utility.SceneManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class RunningLogGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        SaveFileManager.startup();
        // Set primary stage of SceneManager
        SceneManager.setPrimaryStage(primaryStage);
        // Load scenes using SceneManager
        SceneManager.loadScene("main-menu.fxml", AppConstants.MAIN_MENU_SCENE);
        SceneManager.loadScene("configure-storage-settings.fxml", AppConstants.CONFIG_STORAGE_SETTINGS_SCENE);
        SceneManager.loadScene("configure-sql-settings.fxml", AppConstants.CONFIG_SQL_SETTINGS_SCENE);

        // Get scene last opened by user
        String lastOpenedScene = PreferencesManager.get(AppConstants.LAST_SCENE_OPENED_KEY);
        if(lastOpenedScene == null) {
            // Application has not been launched before, no last opened scene
            SceneManager.setScene(AppConstants.MAIN_MENU_SCENE);
        } else {
            SceneManager.setScene(lastOpenedScene);
        }
        // Show primary stage
        SceneManager.showPrimaryStage();

    }
    @Override
    public void stop() {
        // Flush preferences to persistent store
        PreferencesManager.flush();
        // Check for ongoing file operations
        if(SaveFileManager.isFileOperationsOngoing()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ongoing file operations");
            alert.setContentText("Save file operations are still ongoing. Closing the app may result in lost data.");
            ButtonType confirmButton = new ButtonType("Confirm");
            ButtonType cancelButton = new ButtonType("Cancel");
            alert.getButtonTypes().addAll(confirmButton, cancelButton);
            alert.showAndWait().ifPresent(e -> {
                if(e == cancelButton) {
                    // Prevent application from closing
                    Platform.exit();
                }
                // Otherwise, continue to close
            });

        }
    }


}
