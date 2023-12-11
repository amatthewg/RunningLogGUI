module com.example.runningloggui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;


    opens com.aiden to javafx.fxml;
    exports com.aiden.utility;
    opens com.aiden.utility to javafx.fxml;
    exports com.aiden.controllers;
    opens com.aiden.controllers to javafx.fxml;
    exports com.aiden.controllers.mainmenu;
    opens com.aiden.controllers.mainmenu to javafx.fxml;
    exports com.aiden.misc;
    opens com.aiden.misc to javafx.fxml;
}