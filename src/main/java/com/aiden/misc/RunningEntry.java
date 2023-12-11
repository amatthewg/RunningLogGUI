package com.aiden.misc;

import java.text.DecimalFormat;

public class RunningEntry {
    private String runnerName;
    private double distance;
    private double time;
    private double pace;
    private static DecimalFormat df = new DecimalFormat("#.##");

    public RunningEntry(String name, double dist, double time) {
        this.runnerName = name;
        this.distance = dist;
        this.time = time;
        this.pace = time/dist;
    }
    public RunningEntry(String[] values) {
        this.runnerName = values[0];
        this.distance = Double.parseDouble(values[1]);
        this.time = Double.parseDouble(values[2]);
        this.pace = Double.parseDouble(values[3]);
    }
    public String getRunnerName() { return this.runnerName; }
    public double getDistance() { return this.distance; }
    public String getDistanceRounded() { return df.format(this.getDistance()); }
    public double getTime() { return this.time; }
    public double getPace() { return this.pace; }
    public void setRunnerName(String name) { this.runnerName = name; }
    public void setDistance(double dist) {
        this.distance = dist;
        this.pace = this.time/this.distance;
    }
    public void setTime(double time) {
        this.time = time;
        this.pace = this.time/this.distance;
    }
    public String[] getAsStringArray() {
        return new String[]{this.runnerName, ""+this.distance, ""+this.time, ""+this.pace};
    }

}
