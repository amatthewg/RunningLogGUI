package com.aiden.runningloggui.utility;

import com.aiden.runningloggui.RunningEntry;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.prefs.Preferences;

public class SaveFileManager {

    private static File saveFile;
    private static boolean fileOperationOngoing = false;

    public static void startup() {
        // Check Preferences for existence of a save file
        String saveFileLocation = PreferencesManager.get(AppConstants.SAVE_FILE_LOCATION_KEY);
        if(saveFileLocation != null) {
            saveFile = new File(saveFileLocation);
        }
    }
    public static void setFileOperationOngoing(boolean state) {
        fileOperationOngoing = state;
    }
    public static boolean isFileOperationsOngoing() {
        return fileOperationOngoing;
    }

    public static void promptUserForFile() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose CSV save file");
        // Restrict file types
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        fileChooser.getExtensionFilters().addAll(txtFilter, csvFilter);
        // Set directory to parent directory of save file if it already exists
        if(saveFile != null) {
            fileChooser.setInitialDirectory(new File(saveFile.getParent()));
        }
        // Show open dialog
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null) {
            // File has been successfully chosen, set the saveFile object and update preferences
            saveFile = selectedFile;
            String path = saveFile.getAbsolutePath();
            PreferencesManager.put(AppConstants.SAVE_FILE_LOCATION_KEY, path);
        }
    }

    public static boolean saveFileExists() {
        return saveFile != null;
    }

    public static String getSaveFileName() {
        return saveFile.getName();
    }

    // Method to add runner to CSV save file asynchronously
    public static CompletableFuture<Void> writeToSaveFileAsync(List<RunningEntry> runningEntries) {
        // Before starting async operations, indicate file operations ongoing
        fileOperationOngoing = true;
        return CompletableFuture.runAsync(() -> {
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile.getAbsolutePath()))) {
                for(RunningEntry entry : runningEntries) {
                    bw.write(String.join(",", entry.getAsStringArray()));
                }
            } catch(IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Save file error");
                alert.setContentText("Unable to write to save file located at " + saveFile.getAbsolutePath());
                alert.showAndWait();

            }

        });
    }
    public static boolean deleteRunnerByEntry(RunningEntry runningEntry) {

        return false;
    }

    public static List<RunningEntry> readSaveFileAsync() {
        List<RunningEntry> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(saveFile.getAbsolutePath()))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] values = line.split(",");
                result.add(new RunningEntry(values));
            }
            return result;
        } catch(IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save file error");
            alert.setContentText("Unable to read save file located at " + saveFile.getAbsolutePath());
            alert.showAndWait();
            return null;
        }
    }



}
