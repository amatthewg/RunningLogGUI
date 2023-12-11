package com.aiden.controllers;

import com.aiden.utility.AppConstants;
import com.aiden.utility.DatabaseManager;
import com.aiden.utility.PreferencesManager;
import com.aiden.utility.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfigSqlSettingsController implements Initializable {

    @FXML
    RadioButton radioNewDatabase;
    @FXML
    RadioButton radioExistingDatabase;
    @FXML
    TextField newDbNameField;
    @FXML
    Label newDbNameLabel;
    @FXML
    Button newDbUpdateBtn;
    @FXML
    TextField existingDatabaseNameField;
    @FXML
    Label existingDatabaseNameLabel;
    @FXML
    Button existingDatabaseUpdateBtn;
    @FXML
    Button goBackBtn;

    ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize toggle group, add radio buttons
        toggleGroup = new ToggleGroup();
        radioNewDatabase.setToggleGroup(toggleGroup);
        radioExistingDatabase.setToggleGroup(toggleGroup);

        // By default, both toggle buttons will be unselected.
        // The corresponding field and button for each toggle button will
        // be hidden unless that toggle button is selected.

        // Check preferences for selected toggle
        String selectedToggle = PreferencesManager.get(AppConstants.SELECTED_TOGGLE_BUTTON_SQL_SETTINGS);
        if(selectedToggle != null) {
            if(selectedToggle.equals(radioNewDatabase.getText())) {
                radioNewDatabase.setSelected(true);
                showFieldsForNewDb();
            }
            else if(selectedToggle.equals(radioExistingDatabase.getText())) {
                radioExistingDatabase.setSelected(true);
                showFieldsForExistingDb();
            }
        }

        // If no toggle group button is selected, hide these fields
        if(toggleGroup.getSelectedToggle() == null) {
            newDbNameLabel.setVisible(false);
            newDbNameField.setVisible(false);
            newDbUpdateBtn.setVisible(false);
            existingDatabaseNameLabel.setVisible(false);
            existingDatabaseNameField.setVisible(false);
            existingDatabaseUpdateBtn.setVisible(false);
        }

        // Add listener to toggleGroup
        toggleGroup.selectedToggleProperty().addListener(e -> {
            // Get selected toggle button
            RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();
            // Update preferences
            PreferencesManager.put(AppConstants.SELECTED_TOGGLE_BUTTON_SQL_SETTINGS, selectedButton.getText());
            // If selected button is for a new DB, show those fields / hide the others
            if(selectedButton.getText().equals(radioNewDatabase.getText())) {
                showFieldsForNewDb();
            }
            else if(selectedButton.getText().equals(radioExistingDatabase.getText())) {
                showFieldsForExistingDb();
            }

        });
        // Add action event to update button
        existingDatabaseUpdateBtn.setOnAction(e -> {
            String existingDbName = existingDatabaseNameField.getText();
            if (existingDbName == null || existingDbName.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing field");
                alert.setContentText("You must enter a name for the database.");
                alert.showAndWait();
                return;
            }

            // Check if the table already exists
            if (DatabaseManager.tableExists(existingDbName)) {
                // Table exists, update preferences
                PreferencesManager.put(AppConstants.DATABASE_TABLE_NAME, existingDbName);
            } else {
                // Table does not exist, show an error
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The specified table does not exist.");
                alert.showAndWait();
            }
        });
        // Add action to update button
        newDbUpdateBtn.setOnAction(e -> {
            String preferredDbName = newDbNameField.getText();
            if(preferredDbName == null || preferredDbName.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing field");
                alert.setContentText("You must enter a name for the database.");
                alert.showAndWait();
                return;
            }
            PreferencesManager.put(AppConstants.DATABASE_TABLE_NAME, preferredDbName);
        });
        // Add action to go back btn
        goBackBtn.setOnAction(e -> {
            SceneManager.setScene(AppConstants.CONFIG_STORAGE_SETTINGS_SCENE);
        });

    }
    public void toggleGroupListener() {
        RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();

    }
    private void showFieldsForNewDb() {
        newDbNameLabel.setVisible(true);
        newDbNameField.setVisible(true);
        newDbUpdateBtn.setVisible(true);
        existingDatabaseNameLabel.setVisible(false);
        existingDatabaseNameField.setVisible(false);
        existingDatabaseUpdateBtn.setVisible(false);
    }
    private void showFieldsForExistingDb() {
        newDbNameLabel.setVisible(false);
        newDbNameField.setVisible(false);
        newDbUpdateBtn.setVisible(false);
        existingDatabaseNameLabel.setVisible(true);
        existingDatabaseNameField.setVisible(true);
        existingDatabaseUpdateBtn.setVisible(true);
    }
    public String getExistingDbName() {
        return (existingDatabaseNameField == null) ? null : existingDatabaseNameField.getText();
    }

    public String getNewDbName() {
        return (newDbNameField == null) ? null : newDbNameField.getText();
    }
}

