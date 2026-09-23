package com.bilalahmad.repup.utils;

public class HealthCalculator {
    //Calculate BMI
    public static double calculateBMI(double weightKg, double heightCm) {
        if (heightCm <= 0 || weightKg <= 0) return 0.0;
        double heightMeters = heightCm / 100.0;
        double bmi = weightKg / (heightMeters * heightMeters);
        return Math.round(bmi * 10.0) / 10.0; // Round to 1 decimal place
    }

    // Category evaluation
    public static String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "Normal Weight";
        } else if (bmi >= 25 && bmi < 29.9) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    // Mifflin-St Jeor Formula for BMR (Basal Metabolic Rate)
    public static double calculateBMR(double weightKg, double heightCm, int age, boolean isMale) {
        double heightMeters = heightCm / 100.0;
        double bmr;
        if (isMale) {
            return (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5;

    }
        else {
            return (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161;
        }

    }
}
