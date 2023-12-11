package com.aiden.controllers.mainmenu;

import com.aiden.misc.AlertMaker;
import com.aiden.misc.RunningEntry;
import com.aiden.controllers.ConfigSqlSettingsController;
import com.aiden.utility.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainMenuController implements Initializable {
    @FXML
    TextField runnerNameField;
    @FXML
    TextField runnerDistanceField;
    @FXML
    TextField runnerTimeField;
    @FXML
    ScrollPane scrollPane;
    @FXML
    Button addRunBtn;
    @FXML
    RadioButton radioName;
    @FXML
    RadioButton radioDistance;
    @FXML
    RadioButton radioTime;
    @FXML
    RadioButton radioPace;
    @FXML
    Button saveRunsBtn;
    @FXML
    Label storageOptionLabel;
    @FXML
    Button configStorageBtn;
    @FXML
    Button reverseBtn;
    @FXML
    Label saveConfirmationLabel;



    private static boolean reversedListView = false;

    private static List<RunningEntry> runningEntryList;
    private final static RunningEntrySorter sorter = new RunningEntrySorter();

    private ToggleGroup toggleGroup;









    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("DEBUG initialized");
        runningEntryList = new ArrayList<>();
        toggleGroup = new ToggleGroup();
        radioName.setToggleGroup(toggleGroup);
        radioDistance.setToggleGroup(toggleGroup);
        radioTime.setToggleGroup(toggleGroup);
        radioPace.setToggleGroup(toggleGroup);
        setToggleButtonFromPreferences();

        // Add listeners/event handlers
        toggleGroup.selectedToggleProperty().addListener(e -> toggleGroupListener());
        addRunBtn.setOnAction(e -> addRunButtonHandler());
        reverseBtn.setOnAction(e -> reverseButtonHandler());
        configStorageBtn.setOnAction(e -> SceneManager.setScene(AppConstants.CONFIG_STORAGE_SETTINGS_SCENE));

        displayRunningEntryList();
        ControllerManager.setMainMenuControllerInstance(this);

        saveConfirmationLabel.setText("");
        // Load runs from save file or DB
        String selectedStorage = PreferencesManager.get(AppConstants.SELECTED_TOGGLE_BUTTON_STORAGE_SETTINGS);
        if(selectedStorage != null) {
            if(selectedStorage.equals("Local Save File")) {
                CompletableFuture<List<RunningEntry>> future = SaveFileManager.readSaveFileAsync();
                try {
                    runningEntryList = future.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(selectedStorage.equals("SQL Server")) {
                DatabaseManager.startup();
            }
        }

        displayRunningEntryList();
        if(runningEntryList.isEmpty()) {
            saveRunsBtn.setDisable(true);
        }
        refreshStorageLabel();
    }
















    // Handlers / listeners
    private void addRunButtonHandler() {
        if(runnerFieldsAreBlank()) {
            AlertMaker.missingFields();
            return;
        }
        if(!runnerFieldsAreValid()) {
            AlertMaker.invalidInput();
            return;
        }
        if(runnerNameExists(runnerNameField.getText())) {
            AlertMaker.runnerExists();
            return;
        }
        // Tests passed
        // Parse input, create running entry, add to list, sort/display list view
        double distance = Double.parseDouble(runnerDistanceField.getText());
        double time = Double.parseDouble(runnerTimeField.getText());
        RunningEntry entry = new RunningEntry(runnerNameField.getText(), distance, time);
        runningEntryList.add(entry);
        displayRunningEntryList();
        refreshStorageLabel();
    }
    private void reverseButtonHandler() {
        reversedListView = !reversedListView;
        toggleGroupListener();
    }
    private void toggleGroupListener() {
        RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();
        PreferencesManager.put(AppConstants.SELECTED_TOGGLE_BUTTON_MAIN_MENU, selectedButton.getText());
        if(selectedButton.getText().equals(radioName.getText())) {
            sorter.sortByNameAlphabetical(runningEntryList, reversedListView);
            displayRunningEntryList();
        }
        else if(selectedButton.getText().equals(radioDistance.getText())) {
            sorter.sortByDistanceDescending(runningEntryList, reversedListView);
            displayRunningEntryList();
        }
        else if(selectedButton.getText().equals(radioTime.getText())) {
            sorter.sortByTimeAscending(runningEntryList, reversedListView);
            displayRunningEntryList();
        }
        else { // "Pace" button
            sorter.sortByPaceAscending(runningEntryList, reversedListView);
            displayRunningEntryList();
        }
    }














    // Input Check Methods
    private boolean runnerNameExists(String name) {
        for(RunningEntry entry : runningEntryList) {
            if(entry.getRunnerName().strip().equalsIgnoreCase(name.strip())) return true;
        }
        return false;
    }
    private boolean runnerFieldsAreBlank() {
        String runnerName = runnerNameField.getText();
        String runnerDistanceStr = runnerDistanceField.getText();
        String runnerTimeStr = runnerTimeField.getText();
        if(runnerName.isBlank() || runnerDistanceStr.isBlank() || runnerTimeStr.isBlank()) {
            return true;
        }
        return false;
    }
    private boolean runnerFieldsAreValid() {
        String runnerDistanceStr = runnerDistanceField.getText();
        String runnerTimeStr = runnerTimeField.getText();
        String regex = "[^\\d.]"; // NOT 0-9 and NOT decimal points
        System.out.println("DEBUG check if str matches regex:");
        System.out.println("runnerDistStr: " + runnerDistanceStr.matches(regex));
        if(runnerDistanceStr.matches(regex) || runnerTimeStr.matches(regex)) {

            return false;
        }
        return true;
    }







    private void displayRunningEntryList() {

        TimeStringBuilder tsb = new TimeStringBuilder();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(40);
        gridPane.setVgap(10);
        if(runningEntryList.isEmpty()) {
            Label label = new Label("No runs to show.");
            gridPane.getChildren().add(label);
        }
        else {
            Label nameLabel = new Label("Name");
            Label distLabel = new Label("Dist(mi)");
            Label timeLabel = new Label("Time");
            Label paceLabel = new Label("Pace");
            gridPane.add(nameLabel, 0, 0);
            gridPane.add(distLabel, 1, 0);
            gridPane.add(timeLabel, 2, 0);
            gridPane.add(paceLabel, 3, 0);

            for (int row = 0; row < runningEntryList.size(); row++) {
                RunningEntry entry = runningEntryList.get(row);
                Label name = new Label(entry.getRunnerName());
                Label distance = new Label(entry.getDistanceRounded());
                Label time = new Label(tsb.minutesToTimeString(entry.getTime()));
                Label pace = new Label(tsb.minutesToTimeString(entry.getPace()));
                Button moreButton = new Button("More...");
                gridPane.add(name, 0, row+1);
                gridPane.add(distance, 1, row+1);
                gridPane.add(time, 2, row+1);
                gridPane.add(pace, 3, row+1);
                gridPane.add(moreButton, 4, row+1);
                moreButton.setOnAction(e -> handleMoreButton(entry));
            }
        }
        //gridPane.setMaxSize(scrollPane.getHvalue(), scrollPane.getVvalue());
        scrollPane.setContent(gridPane);


        /*
        TableView<Object> tableView = new TableView<>();

        TableColumn<Object, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Object, String> distColumn = new TableColumn<>("Dist(mi)");
        TableColumn<Object, String> timeColumn = new TableColumn<>("Time(hh:mm:ss)");
        TableColumn<Object, String> paceColumn = new TableColumn<>("Pace(mm:ss)");
        TableColumn<>
        nameColumn.getColumns().add(0, "T");
        nameColumn.setPrefWidth(100);
        distColumn.setPrefWidth(60);
        tableView.getColumns().addAll(nameColumn, distColumn, timeColumn);


        scrollPane.setContent(tableView);

         */
    }
    private void handleMoreButton(RunningEntry entry) {
        int listIndex = runningEntryList.indexOf(entry);
        Stage stage = new Stage();
        VBox vbox = new VBox();
        vbox.setSpacing(15);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        Label title = new Label("Edit runner info");
        TextField nameField = new TextField();
        nameField.setText(entry.getRunnerName());
        TextField distanceField = new TextField();
        distanceField.setText(entry.getDistanceRounded());
        TextField timeField = new TextField();
        timeField.setText(""+entry.getTime());
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            String name = nameField.getText();
            String distance = distanceField.getText();
            String time = timeField.getText();
            if(name.isBlank() || distance.isBlank() || time.isBlank()) {
                AlertMaker.missingFields();
                return;
            }
            if(distance.matches("[^\\d.]") || time.matches("[^\\d.]")) {
                AlertMaker.invalidInput();
                return;
            }
            entry.setRunnerName(name);
            entry.setDistance(Double.parseDouble(distance));
            entry.setTime(Double.parseDouble(time));
            stage.close();
            AlertMaker.confirm("Successfully updated");
            displayRunningEntryList();
        });
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete runner");
            alert.setContentText("Delete runner? This action cannot be undone!");
            ButtonType confirmButton = new ButtonType("Confirm");
            ButtonType cancelButton = new ButtonType("Cancel");
            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent() || result.get() != ButtonType.OK) {
                return;
            }
            else {
                runningEntryList.remove(listIndex);
                displayRunningEntryList();
                AlertMaker.confirm("Successfully deleted");
                stage.close();
            }
        });
        vbox.getChildren().addAll(title, nameField, distanceField, timeField,
                updateButton, deleteButton);
        stage.setScene(new Scene(vbox));
        stage.show();
    }















    private void setToggleButtonFromPreferences() {
        // Check preferences for selected toggle button, default to select is 'name'
        String selectedToggle = PreferencesManager.get(AppConstants.SELECTED_TOGGLE_BUTTON_MAIN_MENU);
        if(selectedToggle != null) {
            if(selectedToggle.equals(radioName.getText())) radioName.setSelected(true);
            else if(selectedToggle.equals(radioDistance.getText())) radioDistance.setSelected(true);
            else if(selectedToggle.equals(radioTime.getText())) radioTime.setSelected(true);
            else if(selectedToggle.equals(radioPace.getText())) radioPace.setSelected(true);
        }
        else radioName.setSelected(true);
        toggleGroupListener(); // Invoke listener because it's only invoked when the user clicks a radio button.
    }
    public void refreshStorageLabel() {
        System.out.println("DEBUG calling refresh");
        // Get selected storage option
        String selectedOption = PreferencesManager.get(AppConstants.SELECTED_TOGGLE_BUTTON_STORAGE_SETTINGS);
        System.out.println("DEBUG selectedOption: " + selectedOption);
        if(selectedOption != null) {
            if(selectedOption.equals("Local Save File")) {

                File file = new File(PreferencesManager.get(AppConstants.SAVE_FILE_LOCATION_KEY));
                System.out.println("DEBUG strg label is null: " + (storageOptionLabel == null));
                storageOptionLabel.setText("Using save file " + file.getName());
                storageOptionLabel.setTextFill(Color.GREEN);
                saveRunsBtn.setDisable(false);
            }
            else if(selectedOption.equals("SQL Server")) {
                ConfigSqlSettingsController controller = new ConfigSqlSettingsController();
                String selectedSQLOption = PreferencesManager.get(AppConstants.SELECTED_TOGGLE_BUTTON_SQL_SETTINGS);
                if(selectedSQLOption != null) {
                    if(selectedSQLOption.equals("Program should create a new SQL database to use")) {
                        String name = controller.getNewDbName();
                        if(name == null || name.isBlank()) {
                            storageOptionLabel.setText("SQL configuration needed");
                            storageOptionLabel.setTextFill(Color.RED);
                            saveRunsBtn.setDisable(true);
                        }
                        else {
                            storageOptionLabel.setText("Using SQL table " + AppConstants.DATABASE_TABLE_NAME);
                            storageOptionLabel.setTextFill(Color.GREEN);
                            saveRunsBtn.setDisable(false);
                        }
                    }
                    else if(selectedSQLOption.equals("Use existing database")) {
                        String name = controller.getExistingDbName();
                        if(name == null || name.isBlank()) {
                            storageOptionLabel.setText("SQL configuration needed");
                            saveRunsBtn.setDisable(true);
                        }
                    }
                }


            }
        } else if(selectedOption == null) {
            if(selectedOption.equals("Local Save File")) {

                File file = new File(PreferencesManager.get(AppConstants.SAVE_FILE_LOCATION_KEY));
                System.out.println("DEBUG strg label is null: " + (storageOptionLabel == null));
                storageOptionLabel.setText("Using save file " + file.getName());
                storageOptionLabel.setTextFill(Color.GREEN);
                saveRunsBtn.setDisable(false);
            } else if(selectedOption.equals("SQL Server")) {
                ConfigSqlSettingsController controller = new ConfigSqlSettingsController();
                String selectedSQLOption = PreferencesManager.get(AppConstants.SELECTED_TOGGLE_BUTTON_SQL_SETTINGS);
                if(selectedSQLOption != null) {
                    if(selectedSQLOption.equals("Program should create a new SQL database to use")) {
                        String name = controller.getNewDbName();
                        if(name == null || name.isBlank()) {
                            storageOptionLabel.setText("SQL configuration needed");
                            storageOptionLabel.setTextFill(Color.RED);
                            saveRunsBtn.setDisable(true);
                        }
                        else {
                            storageOptionLabel.setText("Using SQL table " + AppConstants.DATABASE_TABLE_NAME);
                            storageOptionLabel.setTextFill(Color.GREEN);
                            saveRunsBtn.setDisable(false);
                        }
                    }
                    else if(selectedSQLOption.equals("Use existing database")) {
                        String name = controller.getExistingDbName();
                        if(name == null || name.isBlank()) {
                            storageOptionLabel.setText("SQL configuration needed");
                            saveRunsBtn.setDisable(true);
                        }
                    }
                }


            }
        }

        saveRunsBtn.setOnAction(e -> {
            // Get the current date and time
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Format the date and time using a DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            //check if SQL or LSF/ update accordingly
            String selectedStorageMethod = PreferencesManager.get(AppConstants.SELECTED_TOGGLE_BUTTON_STORAGE_SETTINGS);
            if(selectedStorageMethod.equals("Local Save File")) {
                saveConfirmationLabel.setText("Save in progress...");
                CompletableFuture<Void> future = SaveFileManager.writeToSaveFileAsync(runningEntryList);
                future.thenRun(() -> {
                    Platform.runLater(() -> {

                        saveConfirmationLabel.setText("Successfully saved to file @ " + formattedDateTime);
                    });

                });
            }
            else if(selectedStorageMethod.equals("SQL Server")) {

            }
        });
        if(runningEntryList.isEmpty()) {
            saveRunsBtn.setDisable(true);
        }
    }

}
