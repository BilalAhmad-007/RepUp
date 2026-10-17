package com.bilalahmad.repup;

public class DayRhythmItem {
    private String dayName;
    private String workoutTitle;
    private String subText;
    private int iconResId;
    private boolean isSelected;
    private boolean isRestDay;

    public DayRhythmItem(String dayName, String workoutTitle, String subText, int iconResId, boolean isSelected, boolean isRestDay) {
        this.dayName = dayName;
        this.workoutTitle = workoutTitle;
        this.subText = subText;
        this.iconResId = iconResId;
        this.isSelected = isSelected;
        this.isRestDay = isRestDay;
    }

    public String getDayName() { return dayName; }
    public String getWorkoutTitle() { return workoutTitle; }
    public String getSubText() { return subText; }
    public int getIconResId() { return iconResId; }
    public boolean isSelected() { return isSelected; }
    public boolean isRestDay() { return isRestDay; }
}