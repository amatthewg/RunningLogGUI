package com.aiden.runningloggui;

import javafx.scene.control.Alert;



/**
 * @author Aiden Grimsey
 */

public class AlertMaker {

    public static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();

    }

}
