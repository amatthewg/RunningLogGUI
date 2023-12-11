package com.aiden.misc;

import javafx.scene.control.Alert;



/**
 * @author Aiden Grimsey
 */

public class AlertMaker {

    public static void missingFields() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing fields");
        alert.setContentText("Please fill in all fields.");
        alert.showAndWait();
    }
    public static void invalidInput() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setContentText("Please ensure proper input in all fields.");
        alert.showAndWait();
    }
    public static void runnerExists() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Duplicate runner");
        alert.setContentText("A runner with that name already exists.");
        alert.showAndWait();
    }

    public static void confirm(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
