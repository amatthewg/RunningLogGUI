package com.aiden.runningloggui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
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
                System.out.println("LSF selected");
            }
            else if (selectedButton.getText().equals("SQL Server")) {
                System.out.println("SQL selected");
            }
        });
    }
}
