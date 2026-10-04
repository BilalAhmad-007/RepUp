package com.bilalahmad.repup.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_sessions")
public class WorkoutSession {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String workoutTitle; // e.g., "Push day"
    private String muscleGroup;  // e.g., "Chest, Shoulders, Triceps"
    private int exerciseCount;   // e.g., 6
    private int durationMinutes; // e.g., 48
    private long timestamp;      // System timestamp when workout was logged

    // Constructor
    public WorkoutSession(String workoutTitle, String muscleGroup, int exerciseCount, int durationMinutes, long timestamp) {
        this.workoutTitle = workoutTitle;
        this.muscleGroup = muscleGroup;
        this.exerciseCount = exerciseCount;
        this.durationMinutes = durationMinutes;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getWorkoutTitle() { return workoutTitle; }
    public void setWorkoutTitle(String workoutTitle) { this.workoutTitle = workoutTitle; }

    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }

    public int getExerciseCount() { return exerciseCount; }
    public void setExerciseCount(int exerciseCount) { this.exerciseCount = exerciseCount; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
