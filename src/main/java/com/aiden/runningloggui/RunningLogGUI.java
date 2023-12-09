package com.aiden.runningloggui;

import com.aiden.runningloggui.utility.AppConstants;
import com.aiden.runningloggui.utility.PreferencesManager;
import com.aiden.runningloggui.utility.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class RunningLogGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {


        // Set primary stage of SceneManager
        SceneManager.setPrimaryStage(primaryStage);
        // Load scenes using SceneManager
        SceneManager.loadScene("configure-storage-settings.fxml", AppConstants.CONFIG_STORAGE_SETTINGS_SCENE);

        // Get scene last opened by user
        String lastOpenedScene = PreferencesManager.get(AppConstants.LAST_SCENE_OPENED_KEY);
        if(lastOpenedScene == null) {
            // Application has not been launched before, no last opened scene
            SceneManager.setScene(AppConstants.CONFIG_STORAGE_SETTINGS_SCENE);
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
        // TODO check if file/db operations are running
    }
    private static void loadFxmlFiles() {

    }


}
