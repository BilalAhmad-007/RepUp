package com.bilalahmad.repup.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bilalahmad.repup.data.dao.WorkoutDao;
import com.bilalahmad.repup.data.entity.Exercise;
import com.bilalahmad.repup.data.entity.SetRecord;
import com.bilalahmad.repup.data.entity.User;
import com.bilalahmad.repup.data.entity.Workout;

@Database(entities = {User.class, Workout.class, Exercise.class, SetRecord.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    public abstract WorkoutDao workoutDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "repup_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

