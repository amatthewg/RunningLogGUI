package com.aiden.utility;

import com.aiden.controllers.mainmenu.MainMenuController;

public class ControllerManager {
    private static MainMenuController mainMenuControllerInstance;

    public static void setMainMenuControllerInstance(MainMenuController controller) {
        mainMenuControllerInstance = controller;
    }

    public static MainMenuController getMainMenuControllerInstance() {
        return mainMenuControllerInstance;
    }
}
