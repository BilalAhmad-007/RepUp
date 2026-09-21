package com.bilalahmad.repup.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bilalahmad.repup.data.dao.WorkoutDao;
import com.bilalahmad.repup.data.database.AppDatabase;
import com.bilalahmad.repup.data.entity.Exercise;
import com.bilalahmad.repup.data.entity.SetRecord;
import com.bilalahmad.repup.data.entity.Workout;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkoutRepository {
    private final WorkoutDao workoutDao;
    private final LiveData<List<Workout>> allWorkouts;
    private final ExecutorService executorService;

    public WorkoutRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        workoutDao = database.workoutDao();
        allWorkouts = workoutDao.getAllWorkouts();
        executorService = Executors.newFixedThreadPool(4);
    }
    public LiveData<List<Workout>> getAllWorkouts(){
        return allWorkouts;
    }
    public LiveData<List<Exercise>> getExercisesByMuscleGroup(String muscleGroup){
        return workoutDao.getExercisesByMuscleGroup(muscleGroup);
    }
    public LiveData<List<SetRecord>> getSetsForWorkout(int workoutId){
        return workoutDao.getSetsForWorkout(workoutId);
    }
  public LiveData<Double> getMaxWeightForExercise(int exerciseId){
        return workoutDao.getMaxWeightForExercise(exerciseId);
  }
  public void insertWorkout(Workout workout, OnWorkoutInsertedCallback callback){
        executorService.execute(() -> {
            long newWorkoutId = workoutDao.insertWorkout(workout);
            if(callback != null){
                    callback.onWorkoutInserted((int)newWorkoutId);
            }
        });
  }
  public void insertExercise(Exercise exercise) {
      executorService.execute(() -> workoutDao.insertExercise(exercise));
  }
  public void insertSetRecord(SetRecord setRecord) {
      executorService.execute(() -> workoutDao.insertSetRecord(setRecord));
  }
  public interface OnWorkoutInsertedCallback {
      void onWorkoutInserted(int workoutId);
  }

}
