package com.bilalahmad.repup.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bilalahmad.repup.data.dao.UserMetricDao;
import com.bilalahmad.repup.data.entity.UserMetricEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {WorkoutSession.class, UserMetricEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WorkoutDao workoutDao();
    public abstract UserMetricDao userMetricDao();

    private static volatile AppDatabase INSTANCE;

    // Background Executor to run database operations off the main UI thread
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "repup_database"
                    )
                    .fallbackToDestructiveMigration(true)
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
