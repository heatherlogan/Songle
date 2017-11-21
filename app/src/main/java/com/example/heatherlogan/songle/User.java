package com.example.heatherlogan.songle;

public class User {

    private String name;
    private String level;
    private long time;
    private int steps;

    public User (String name, String level, long time, int steps){
        this.name = name;
        this.level = level;
        this.time = time;
        this.steps = steps;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getUserLevel() {
        return level;
    }

    public void setUserLevel(String level) {
        this.level = level;
    }

    public long getUserTime() {
        return time;
    }

    public void setUserTime(long time) {
        this.time = time;
    }

    public int getUserSteps() {
        return steps;
    }

    public void setUserSteps(int steps) {
        this.steps = steps;
    }

}
