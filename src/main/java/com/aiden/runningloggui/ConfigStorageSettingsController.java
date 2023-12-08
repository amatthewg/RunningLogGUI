package com.aiden.runningloggui;

import com.aiden.runningloggui.utility.AppConstants;
import com.aiden.runningloggui.utility.PreferencesManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.URL;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ResourceBundle;

public class ConfigStorageSettingsController implements Initializable {

    // Get references to FXML elements
    @FXML
    RadioButton radioLocalSaveFile;
    @FXML
    RadioButton radioSQL;
    @FXML
    Label saveFileLocationLabel;
    @FXML
    Button changeLSFBtn;
    @FXML
    TextField sqlServerAddrField;
    @FXML
    TextField sqlServerUserField;
    @FXML
    PasswordField sqlServerPassField;
    @FXML
    Button sqlServerConnectBtn;
    @FXML
    Label sqlConnStatusLabel;
    @FXML
    Button goBackBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add the two radio buttons to one ToggleGroup
        ToggleGroup toggleGroup = new ToggleGroup();
        radioSQL.setToggleGroup(toggleGroup);
        radioLocalSaveFile.setToggleGroup(toggleGroup);

        // Add listener to the ToggleGroup
        toggleGroup.selectedToggleProperty().addListener(e -> {
            RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();
            if (selectedButton.getText().equals("Local Save File")) {
                // If Local Save File option is selected, prevent editing in the SQL section
                sqlServerAddrField.setEditable(false);
                sqlServerUserField.setEditable(false);
                sqlServerPassField.setEditable(false);
                sqlServerConnectBtn.setDisable(true);
            }
            else if (selectedButton.getText().equals("SQL Server")) {
                // If SQL option is selected, allow editing in the SQL section
                sqlServerAddrField.setEditable(true);
                sqlServerUserField.setEditable(true);
                sqlServerPassField.setEditable(true);
                sqlServerConnectBtn.setDisable(false);
            }
        });

        // Add action to Change LSF Button
        changeLSFBtn.setOnAction(e -> {
            // TODO SaveFileManager prompt user for save file
        });

        // Logic for Save File Location label
        // Upon initialization, check preferences for save file location
        String saveFileLocation = PreferencesManager.get(AppConstants.SAVE_FILE_LOCATION_KEY);
        if(saveFileLocation == null) {
            saveFileLocationLabel.setText("No save file selected");
            saveFileLocationLabel.setTextFill(Color.RED);
        }
        else {
            File file = new File(saveFileLocation);
            saveFileLocationLabel.setText(file.getName());
        }

        // Populate SQL fields with Preference data
        sqlServerAddrField.setText(PreferencesManager.get(AppConstants.SQL_SERVER_ADDR_KEY));
        sqlServerUserField.setText(PreferencesManager.get(AppConstants.SQL_SERVER_USER_KEY));
        sqlServerPassField.setText(PreferencesManager.get(AppConstants.SQL_SERVER_PASS_KEY));

        // Add action to SQL connect button
        sqlServerConnectBtn.setOnAction(e -> {
            // Get connection details
            String serverAddress = sqlServerAddrField.getText();
            String serverUser = sqlServerUserField.getText();
            String serverPass = sqlServerPassField.getText();
            if(serverAddress.isBlank() || serverUser.isBlank() || serverPass.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incomplete fields");
                alert.setContentText("All SQL fields must be filled in.");
                alert.showAndWait();
                return;
            }
            // Update preferences with server details
            PreferencesManager.put(AppConstants.SQL_SERVER_ADDR_KEY, serverAddress);
            PreferencesManager.put(AppConstants.SQL_SERVER_USER_KEY, serverUser);
            PreferencesManager.put(AppConstants.SQL_SERVER_PASS_KEY, serverPass);
            // TODO db manager establish connection
        });

        // Add action to go back button

    }
}
