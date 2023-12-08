package com.aiden.runningloggui.utility;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.prefs.Preferences;

public class SaveFileManager {



    private static File saveFile;

    public static void promptUserForFile() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose CSV save file");
        // Restrict file types
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        fileChooser.getExtensionFilters().addAll(txtFilter, csvFilter);
        // Show open dialog
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null) {
            saveFile = selectedFile;
        }
    }

    public static boolean saveFileExists() {
        return saveFile.exists();
    }

    public static String getSaveFileName() {
        return saveFile.getName();
    }

    // Method to add runner to CSV save file. Returns false if runner with that name already exists.
    public static boolean addRunnerByName(String name, double dist, double time, double pace) {

        return false;
    }
    public static boolean deleteRunnerByName(String name) {

        return false;
    }




}
