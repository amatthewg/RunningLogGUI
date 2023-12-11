package com.aiden.utility;

import com.aiden.misc.RunningEntry;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;

    public static void setDbUrl(String url) {
        dbUrl = url;
    }
    public static void setDbUsername(String username) {
        dbUsername = username;
    }
    public static void setDbPassword(String password) {
        dbPassword = password;
    }
    public static void startup() {
        readPreferences();
        if (databaseIsConnected(dbUrl, dbUsername, dbPassword)) {
            String tableName = PreferencesManager.get(AppConstants.DATABASE_TABLE_NAME);
            if (tableName != null && !tableName.isBlank() && !tableExists(tableName)) {
                createNewTable();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Connection Successful");
            alert.setContentText("Connection to the SQL server is successful.");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Failed");
            alert.setContentText("Unable to connect to the SQL server. Please check your credentials.");
            alert.showAndWait();
        }
    }
    public static boolean credentialsExist() {
        return !(dbUsername == null || dbUrl == null || dbPassword == null);
    }
    public static void createNewTable() {
        String tableName = PreferencesManager.get(AppConstants.DATABASE_TABLE_NAME);

        if (tableName != null && !tableName.isBlank()) {
            try {
                Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                Statement statement = conn.createStatement();

                // Define your table creation SQL statement
                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                        + "ID INT PRIMARY KEY AUTO_INCREMENT,"
                        + "RUNNER_NAME VARCHAR(255),"
                        + "DISTANCE DOUBLE,"
                        + "TIME DOUBLE,"
                        + "PACE DOUBLE"
                        + ")";

                statement.executeUpdate(createTableSQL);

                conn.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error creating table: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Database table name is missing.");
            alert.showAndWait();
        }
    }
    public static boolean tableExists(String tableName) {
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, null);
            boolean tableExists = tables.next();
            conn.close();
            return tableExists;
        } catch (SQLException e) {
            return false;
        }
    }
    private static void readPreferences() {
        // Read preferences and initialize dbUrl, dbUsername, and dbPassword
        dbUrl = PreferencesManager.get(AppConstants.SQL_SERVER_ADDR_KEY);
        dbUsername = PreferencesManager.get(AppConstants.SQL_SERVER_USER_KEY);
        dbPassword = PreferencesManager.get(AppConstants.SQL_SERVER_PASS_KEY);

    }

    private static void updatePreferences() {
        // Update preferences with the current values of dbUrl, dbUsername, and dbPassword
        PreferencesManager.put(AppConstants.SQL_SERVER_ADDR_KEY, dbUrl);
        PreferencesManager.put(AppConstants.SQL_SERVER_USER_KEY, dbUsername);
        PreferencesManager.put(AppConstants.SQL_SERVER_PASS_KEY, dbPassword);
    }

    public static boolean databaseIsConnected(String serverAddress, String username, String password) {
        try {
            Connection conn = DriverManager.getConnection(serverAddress, username, password);
            conn.close(); // Close the connection if successful
            return true;
        } catch (SQLException e) {
            return false; // Connection failed
        }
    }

    public static List<RunningEntry> readFromDb() {
        String tableName = PreferencesManager.get(AppConstants.DATABASE_TABLE_NAME);
        String sqlQuery = "SELECT * FROM " + tableName;
        List<RunningEntry> runningEntries = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                String runnerName = resultSet.getString("RUNNER_NAME");
                double distance = resultSet.getDouble("DISTANCE");
                double time = resultSet.getDouble("TIME");

                RunningEntry entry = new RunningEntry(runnerName, distance, time);
                runningEntries.add(entry);
            }

            conn.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error reading from database: " + e.getMessage());
            alert.showAndWait();
        }

        return runningEntries;
    }


    public static void uploadEntriesToDb(List<RunningEntry> list) {
        String tableName = PreferencesManager.get(AppConstants.DATABASE_TABLE_NAME);

        // Delete all entries from the table
        String deleteQuery = "DELETE FROM " + tableName;

        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            Statement deleteStatement = conn.createStatement();
            deleteStatement.executeUpdate(deleteQuery);
            conn.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error deleting entries from the database: " + e.getMessage());
            alert.showAndWait();
            return; // Stop further processing if deletion fails
        }

        // Upload new entries to the table
        String insertQuery = "INSERT INTO " + tableName + " (RUNNER_NAME, DISTANCE, TIME, PACE) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);

            for (RunningEntry entry : list) {
                insertStatement.setString(1, entry.getRunnerName());
                insertStatement.setDouble(2, entry.getDistance());
                insertStatement.setDouble(3, entry.getTime());
                insertStatement.setDouble(4, entry.getPace());

                insertStatement.executeUpdate();
            }

            conn.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error uploading entries to the database: " + e.getMessage());
            alert.showAndWait();
        }
    }

}
