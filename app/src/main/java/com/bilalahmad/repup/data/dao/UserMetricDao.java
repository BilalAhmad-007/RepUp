package com.bilalahmad.repup.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bilalahmad.repup.data.entity.UserMetricEntity;

import java.util.List;

@Dao
public interface UserMetricDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMetric(UserMetricEntity metric);

    @Query("SELECT * FROM user_metrics ORDER BY timestamp DESC")
    List<UserMetricEntity> getAllMetrics();

    @Query("SELECT * FROM user_metrics ORDER BY timestamp DESC LIMIT 1")
    UserMetricEntity getLatestMetric();
}
