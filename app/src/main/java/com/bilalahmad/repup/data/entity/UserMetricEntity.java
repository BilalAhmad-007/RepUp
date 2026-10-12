package com.bilalahmad.repup.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_metrics")
public class UserMetricEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private int age;
    private double heightCm;
    private double weightKg;
    private String sex;
    private double bmi;
    private double bmr;
    private double tdee;
    private long timestamp;

    public UserMetricEntity(int age, double heightCm, double weightKg, String sex, double bmi, double bmr, double tdee, long timestamp) {
        this.age = age;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.sex = sex;
        this.bmi = bmi;
        this.bmr = bmr;
        this.tdee = tdee;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getAge() { return age; }
    public double getHeightCm() { return heightCm; }
    public double getWeightKg() { return weightKg; }
    public String getSex() { return sex; }
    public double getBmi() { return bmi; }
    public double getBmr() { return bmr; }
    public double getTdee() { return tdee; }
    public long getTimestamp() { return timestamp; }
}
