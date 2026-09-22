package com.bilalahmad.repup.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.bilalahmad.repup.data.entity.SetRecord;
import com.bilalahmad.repup.data.entity.Workout;

import java.util.List;


public class WorkoutWithSets {
    @Embedded
    public Workout workout;
    @Relation(parentColumn = "workoutId", entityColumn = "workoutOwnerId")
    public List<SetRecord> setRecords;
}
