package com.example.heatherlogan.songle;

import java.util.Comparator;

public class User {

    private String name;
    private String level;
    private int time;
    private int steps;

    public User (){

    }

    public User (String name, String level, int time){
        this.name = name;
        this.level = level;
        this.time = time;
    }

    public User (String name, String level, int time, int steps){
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

    public int getUserTime() {
        return time;
    }

    public void setUserTime(int time) {
        this.time = time;
    }

    public int getUserSteps() {
        return steps;
    }

    public void setUserSteps(int steps) {
        this.steps = steps;
    }

    public static Comparator<User> UserComparator = new Comparator<User>(){

        public int compare(User u1, User u2) {

            int time1 = u1.getUserTime();
            int time2 = u1.getUserTime();

           return time1 - time2;
        }
    };


}
