package com.aiden.runningloggui.utility;

import javafx.scene.control.Alert;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesManager {
    private final static Preferences PREFERENCES = Preferences.userNodeForPackage(PreferencesManager.class);

    public static String get(String key) {
        return PREFERENCES.get(key, null);
    }
    public static void put(String key, String value) {
        PREFERENCES.put(key, value);
    }
    public static void flush() {
        try {
            PREFERENCES.flush();
        } catch(BackingStoreException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Failed to save application state");
            alert.setContentText("Failed to flush user preferences object in preferences manager.");
            alert.showAndWait();
        }

    }
}
