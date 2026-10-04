package com.bilalahmad.repup.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkoutDao {
    // Insert a new workout session
    @Insert
    void insertWorkout(WorkoutSession session);

    @Query("SELECT * FROM workout_sessions ORDER BY timestamp DESC")
    LiveData<List<WorkoutSession>> getAllWorkouts();

    @Query("SELECT COUNT(*) FROM workout_sessions")
    LiveData<Integer> getTotalWorkoutsCount();

    @Query("SELECT SUM(durationMinutes) FROM workout_sessions")
    LiveData<Integer> getTotalDurationMinutes();

}
