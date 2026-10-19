package com.bilalahmad.repup.model;

public class DayChipItem {
    private String dayName;
    private int exerciseCount;
    private boolean isRest;
    private boolean isSelected;

    public DayChipItem(String dayName, int exerciseCount, boolean isRest, boolean isSelected) {
        this.dayName = dayName;
        this.exerciseCount = exerciseCount;
        this.isRest = isRest;
        this.isSelected = isSelected;
    }

    public String getDayName() { return dayName; }
    public int getExerciseCount() { return exerciseCount; }
    public boolean isRest() { return isRest; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }

}
