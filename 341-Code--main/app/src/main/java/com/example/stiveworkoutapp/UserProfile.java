package com.example.stiveworkoutapp;

public class UserProfile {
    private String name;
    private String bio;
    private int steps;
    private int sleepHours;
    private int caloriesBurned;

    public UserProfile(String name, String bio, int steps, int sleepHours, int caloriesBurned) {
        this.name = name;
        this.bio = bio;
        this.steps = steps;
        this.sleepHours = sleepHours;
        this.caloriesBurned = caloriesBurned;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public int getSteps() {
        return steps;
    }

    public int getSleepHours() {
        return sleepHours;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }
}