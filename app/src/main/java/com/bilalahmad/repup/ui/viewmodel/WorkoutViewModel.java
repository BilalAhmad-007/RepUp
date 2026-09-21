package com.bilalahmad.repup.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bilalahmad.repup.data.entity.Exercise;
import com.bilalahmad.repup.data.entity.SetRecord;
import com.bilalahmad.repup.data.entity.Workout;
import com.bilalahmad.repup.data.repository.WorkoutRepository;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private final WorkoutRepository repository;
    private final LiveData<List<Workout>> allWorkouts;
    private final MutableLiveData<Integer> currentWorkoutId=new MutableLiveData<>(-1);
    private final MutableLiveData<Double> currentWorkoutVolume = new MutableLiveData<>(0.0);
    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
        allWorkouts = repository.getAllWorkouts();
    }
    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }
    public LiveData<Integer> getCurrentWorkoutId(){
        return currentWorkoutId;
    }
    public LiveData<Double> getCurrentWorkoutVolume(){
        return currentWorkoutVolume;
    }

    public void startNewWorkout(String title){
        Workout newWorkout = new Workout(title, System.currentTimeMillis(),0);
        repository.insertWorkout(newWorkout, workoutId -> currentWorkoutId.postValue(workoutId));
    }

    public void logSet(int exerciseId, int setNumber, double weight, int reps){
        Integer activeWorkoutId = currentWorkoutId.getValue();
        if(activeWorkoutId!=null && activeWorkoutId!=-1){
            SetRecord record = new SetRecord(activeWorkoutId, exerciseId, setNumber, weight, reps);
            repository.insertSetRecord(record);

            double addedVolume = weight * reps;
            Double currentVol = currentWorkoutVolume.getValue();
            if(currentVol!=null){
                currentWorkoutVolume.postValue(currentVol+addedVolume);
            }

        }
    }

    public void addDefaultExercise(String name, String muscleGroup){
        repository.insertExercise(new Exercise(name, muscleGroup));
    }

}
