package com.habit.palmapp.models;

public class Habit {

    private String habit;
    private String habitid;
    private boolean status;

    public Habit() {

    }

    public Habit(String habit, String habitid, boolean status) {
        this.habit = habit;
        this.habitid = habitid;
        this.status = status;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public String getHabitid() {
        return habitid;
    }

    public void setHabitid(String habitid) {
        this.habitid = habitid;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}