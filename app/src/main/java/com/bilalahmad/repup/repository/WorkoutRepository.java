package com.bilalahmad.repup.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bilalahmad.repup.database.AppDatabase;
import com.bilalahmad.repup.database.WorkoutDao;
import com.bilalahmad.repup.database.WorkoutSession;

import java.util.List;

public class WorkoutRepository {
    private final WorkoutDao workoutDao;
    private final LiveData<List<WorkoutSession>> allWorkouts;
    private final LiveData<Integer> totalWorkoutsCount;
    private final LiveData<Integer> totalDurationMinutes;

    public WorkoutRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        workoutDao = db.workoutDao();
        allWorkouts = workoutDao.getAllWorkouts();
        totalWorkoutsCount = workoutDao.getTotalWorkoutsCount();
        totalDurationMinutes = workoutDao.getTotalDurationMinutes();
    }

    public LiveData<List<WorkoutSession>> getAllWorkouts() {
        return allWorkouts;
    }

    public LiveData<Integer> getTotalWorkoutsCount() {
        return totalWorkoutsCount;
    }

    public LiveData<Integer> getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public void insert(WorkoutSession session) {
        AppDatabase.databaseWriteExecutor.execute(() -> workoutDao.insertWorkout(session));
    }
}
