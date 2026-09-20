package com.bilalahmad.repup.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.bilalahmad.repup.data.entity.Exercise;
import com.bilalahmad.repup.data.entity.SetRecord;
import com.bilalahmad.repup.data.entity.Workout;

@Dao
public interface WorkoutDao {
    // Insert a new workout
    @Insert
    void insertWorkout(Workout workout);

    @Insert
    void insertExercise(Exercise exercise);

    @Insert
    void insertSetRecord(SetRecord setRecord);

//    @Query("SELECT * FROM workouts")
}
