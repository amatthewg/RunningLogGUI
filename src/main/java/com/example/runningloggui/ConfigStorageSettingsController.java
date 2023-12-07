package com.example.runningloggui;

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

    }
}
