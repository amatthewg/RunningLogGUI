module com.example.runningloggui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.runningloggui to javafx.fxml;
    exports com.example.runningloggui;
}