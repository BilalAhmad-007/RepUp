package com.bilalahmad.repup.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bilalahmad.repup.database.WorkoutSession;
import com.bilalahmad.repup.repository.WorkoutRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final WorkoutRepository repository;
    private final LiveData<List<WorkoutSession>> allWorkouts;
    private final LiveData<Integer> totalWorkoutsCount;
    private final LiveData<Integer> totalDurationMinutes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
        allWorkouts = repository.getAllWorkouts();
        totalWorkoutsCount = repository.getTotalWorkoutsCount();
        totalDurationMinutes = repository.getTotalDurationMinutes();
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

    public void insertWorkout(WorkoutSession session) {
        repository.insert(session);
    }
}
