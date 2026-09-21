package com.bilalahmad.repup.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "set_records",
    foreignKeys = {
        @ForeignKey(
                entity = Workout.class,
                parentColumns = "workoutId",
                childColumns = "workoutOwnerId",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Exercise.class,
                parentColumns = "exerciseId",
                childColumns = "exerciseOwnerId",
                onDelete = ForeignKey.CASCADE
        )
    }
)
public class SetRecord {
    @PrimaryKey(autoGenerate = true)
    private int setRecordId;
    private double weight;
    private int setNumber;
    private int reps;
    private int workoutOwnerId;
    private int exerciseOwnerId;

    // Constructor
    public SetRecord(int workoutOwnerId, int exerciseOwnerId, int setNumber, double weight, int reps) {
        this.weight = weight;
        this.setNumber = setNumber;
        this.reps = reps;
        this.workoutOwnerId = workoutOwnerId;
        this.exerciseOwnerId = exerciseOwnerId;

    }

    // Getters and Setters
    public int getSetRecordId() {
        return setRecordId;
    }
    public void setSetRecordId(int setRecordId) {
        this.setRecordId = setRecordId;
    }
    public int getWorkoutOwnerId() { return workoutOwnerId; }
    public int getExerciseOwnerId() { return exerciseOwnerId; }
    public int getSetNumber() { return setNumber; }
    public double getWeight() { return weight; }
    public int getReps() { return reps; }
}
