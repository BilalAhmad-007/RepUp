package com.bilalahmad.repup;

import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import com.bilalahmad.repup.database.WorkoutSession;
import com.bilalahmad.repup.databinding.ActivityMainBinding;
import com.bilalahmad.repup.viewmodel.MainViewModel;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        observeViewModel(); //for workout count and duration update

        setUpClickListeners();
        setUpBottomNavigation();

    }
    private void observeViewModel() {
        // Observe total workouts count
        viewModel.getTotalWorkoutsCount().observe(this, count -> {
            if (count != null) {
                // Update UI text
            }
        });

        // Observe total workout duration
        viewModel.getTotalDurationMinutes().observe(this, minutes -> {
            if (minutes != null) {
                // Update UI text
            }
        });
    }

    private void setUpClickListeners() {
        binding.btnStartWorkout.setOnClickListener(v -> {
            //Insert test workout session into rdb
            WorkoutSession sampleWorkout = new WorkoutSession(
                    "Push day",
                    "Chest, Shoulders, Triceps",
                    6,
                    48,
                    System.currentTimeMillis()
            );
            viewModel.insertWorkout(sampleWorkout);

            Toast.makeText(this, "Workout saved to Room database!", Toast.LENGTH_SHORT).show();

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