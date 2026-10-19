package com.bilalahmad.repup.model;

public class ExerciseItem {
    private String exerciseName;
    private String setsReps;
    private boolean isChecked;

    public ExerciseItem(String exerciseName, String setsReps, boolean isChecked) {
        this.exerciseName = exerciseName;
        this.setsReps = setsReps;
        this.isChecked = isChecked;
    }

    public String getExerciseName() { return exerciseName; }
    public String getSetsReps() { return setsReps; }
    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }

}
