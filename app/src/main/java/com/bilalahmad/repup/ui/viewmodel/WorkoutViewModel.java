package com.bilalahmad.repup.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WorkoutViewModel extends AndroidViewModel {
    private final MutableLiveData<Double> currentWorkoutVolume = new MutableLiveData<>(0.0);

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Double> getCurrentWorkoutVolume() {
        return currentWorkoutVolume;
    }

    public void setCurrentWorkoutVolume(double volume) {
        currentWorkoutVolume.setValue(volume);
    }
}
