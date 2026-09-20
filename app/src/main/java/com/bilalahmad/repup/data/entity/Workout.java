package com.bilalahmad.repup.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workouts")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private int workoutId;
    private String title;
    private long timestamp;
    private int durationSeconds;

    // Constructor
    public Workout(String title, long timestamp, int durationSeconds) {
        this.title = title;
        this.timestamp = timestamp;
        this.durationSeconds = durationSeconds;
    }

    // Getters and Setters
    public int getWorkoutId() {
        return workoutId;
    }
    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public int getDurationSeconds() {
        return durationSeconds;
    }
    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

}
