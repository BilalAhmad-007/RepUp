package com.bilalahmad.repup.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.bilalahmad.repup.data.entity.Exercise;
import com.bilalahmad.repup.data.entity.SetRecord;
import com.bilalahmad.repup.data.entity.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    // Insert a new workout
    @Insert
    void insertWorkout(Workout workout);

    @Insert
    void insertExercise(Exercise exercise);

    @Insert
    void insertSetRecord(SetRecord setRecord);

   @Query("SELECT * FROM workouts ORDER BY timestamp DESC")
   LiveData<List<Workout>> getAllWorkouts();

   @Query("SELECT * FROM exercises WHERE muscleGroup = :muscleGroup")
   LiveData<List<Exercise>> getExercisesByMuscleGroup(String muscleGroup);

   @Query("SELECT * FROM set_records WHERE workoutOwnerId = :workoutId")
   LiveData<List<SetRecord>> getSetsForWorkout(int workoutId);

   @Query("SELECT MAX(weight) FROM set_records WHERE exerciseOwnerId = :exerciseId")
    LiveData<Double> getMaxWeightForExercise(int exerciseId);
}
