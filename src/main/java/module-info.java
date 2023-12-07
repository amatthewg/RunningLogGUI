module com.example.runningloggui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;


    opens com.aiden.runningloggui to javafx.fxml;
    exports com.aiden.runningloggui;
}