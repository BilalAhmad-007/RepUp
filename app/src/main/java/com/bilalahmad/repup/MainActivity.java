package com.bilalahmad.repup;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bilalahmad.repup.databinding.ActivityMainBinding;
import com.bilalahmad.repup.ui.fragment.ActiveWorkoutFragment;
import com.bilalahmad.repup.ui.fragment.BmiCalculatorFragment;
import com.bilalahmad.repup.ui.fragment.HistoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpClickListeners();
        setUpBottomNavigation();

    }

    private void setUpClickListeners() {
        binding.btnStartWorkout.setOnClickListener(v -> {
            Toast.makeText(this, "Starting Push Day Workout...", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Workout Execution Screen
        });
        binding.btnCustomizePlan.setOnClickListener(v -> {
            Toast.makeText(this, "Customize Plan Button Clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Customize Plan Screen
        });
        binding.btnNotification.setOnClickListener(v -> {
            Toast.makeText(this, "Notification Button Clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Notification Screen
        });
    }

    private void setUpBottomNavigation(){
        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_history) {
                Toast.makeText(this, "History Tab Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_metrics) {
                Toast.makeText(this, "Metrics Tab Selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }


}