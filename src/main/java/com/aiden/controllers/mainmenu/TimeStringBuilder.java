package com.aiden.controllers.mainmenu;

import java.util.Arrays;

public class TimeStringBuilder {
    public TimeStringBuilder() {

    }

    // Returns String of format hh:mm:ss
    public String minutesToTimeString(double input) {
        int hours = (int) input / 60;
        int minutes = (int) input % 60;
        int seconds = (int) ((input % 1) * 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
