package com.aiden.runningloggui;

import java.io.File;
import java.util.prefs.Preferences;

public class SaveFileManager {

    private static final String SAVE_FILE_LOCATION_KEY = "saveFileLocation";

    private static File saveFile;

    public static void setSaveFileLocation(String location) {
        Preferences prefs = Preferences.userNodeForPackage(SaveFileManager.class);
        prefs.put(SAVE_FILE_LOCATION_KEY, location);
    }

    public static String loadSaveFileLocation() {
        Preferences prefs = Preferences.userNodeForPackage(SaveFileManager.class);
        return prefs.get(SAVE_FILE_LOCATION_KEY, null);
    }




}
