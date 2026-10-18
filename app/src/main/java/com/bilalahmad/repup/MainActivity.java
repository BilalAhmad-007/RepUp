package com.bilalahmad.repup;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bilalahmad.repup.adapter.WeeklyRhythmAdapter;
import com.bilalahmad.repup.database.WorkoutSession;
import com.bilalahmad.repup.databinding.ActivityMainBinding;
import com.bilalahmad.repup.ui.HistoryFragment;
import com.bilalahmad.repup.ui.MetricsFragment;
import com.bilalahmad.repup.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private String currentActivePlan = "Push / Pull / Legs";
    private static final String PREFS_NAME = "RepUpPrefs";
    private static final String KEY_ACTIVE_PLAN = "active_plan";
    private WeeklyRhythmAdapter weeklyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            v.setPadding(v.getPaddingLeft(), statusBarHeight, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });



        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        observeViewModel(); //for workout count and duration update

        currentActivePlan = getSavedPlan();

        setUpClickListeners();
        setUpBottomNavigation();
        setupWeeklyRhythmRecyclerView();

        updatePlanCardsUI(currentActivePlan);

    }
    private void setupWeeklyRhythmRecyclerView() {
        weeklyAdapter = new WeeklyRhythmAdapter();
        binding.rvWeeklyRhythm.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvWeeklyRhythm.setAdapter(weeklyAdapter);

        int todayPosition = getTodayPositionIndex();
        if (todayPosition != -1) {
            binding.rvWeeklyRhythm.scrollToPosition(todayPosition);
        }
    }
    private int getTodayPositionIndex() {
      java.util.Calendar calendar = java.util.Calendar.getInstance();
      int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);

      switch (dayOfWeek) {
            case java.util.Calendar.TUESDAY: return 1;
            case java.util.Calendar.WEDNESDAY: return 2;
            case java.util.Calendar.THURSDAY: return 3;
            case java.util.Calendar.FRIDAY: return 4;
            case java.util.Calendar.SATURDAY: return 5;
            case java.util.Calendar.SUNDAY: return 6;
          case java.util.Calendar.MONDAY:
          default: return 0;
      }
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

        //plan card listeners
        binding.cardPlanPpl.setOnClickListener(v -> handlePlanClick("Push / Pull / Legs"));
        binding.cardPlanFullBody.setOnClickListener(v -> handlePlanClick("Full Body Foundation"));
        binding.cardPlanUpperLower.setOnClickListener(v -> handlePlanClick("Upper / Lower Split"));
    }
    private void handlePlanClick(String clickedPlan) {
        // if already selected plan, show toast
        if (clickedPlan.equals(currentActivePlan)) {
            Toast.makeText(this, clickedPlan + " is already your active plan", Toast.LENGTH_SHORT).show();
            return;
        }

        //if another
        showPlanChangeConfirmation(clickedPlan);
    }

    private void setUpBottomNavigation(){

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                binding.mainHomeContent.setVisibility(View.VISIBLE);
                binding.fragmentContainer.setVisibility(View.GONE);
                return true;
            } else if (itemId == R.id.nav_history) {
                binding.mainHomeContent.setVisibility(View.GONE);
                binding.fragmentContainer.setVisibility(View.VISIBLE);
                loadFragment(new HistoryFragment());
                return true;
            } else if (itemId == R.id.nav_metrics) {
                binding.mainHomeContent.setVisibility(View.GONE);
                binding.fragmentContainer.setVisibility(View.VISIBLE);
                loadFragment(new MetricsFragment());
                return true;
            }
            return false;
        });
    }
    private void showPlanChangeConfirmation(String planName) {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("Switch Active Plan?")
                .setMessage("Do you want to switch your active workout plan to '" + planName + "'?")
                .setPositiveButton("Switch", (dialog, which) -> {

                    currentActivePlan = planName;

                    saveSelectedPlan(currentActivePlan);

                    updatePlanCardsUI(planName);

                    Toast.makeText(this, "Active plan switched to " + planName, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void updatePlanCardsUI(String activePlan) {

        binding.cardPlanPpl.setBackgroundResource(R.drawable.bg_white_card);
        binding.cardPlanFullBody.setBackgroundResource(R.drawable.bg_white_card);
        binding.cardPlanUpperLower.setBackgroundResource(R.drawable.bg_white_card);

        resetBadge(binding.tvBadgePpl, "Advance");
        resetBadge(binding.tvBadgeFullBody, "Beginner");
        resetBadge(binding.tvBadgeUpperLower, "Intermediate");

        resetIcon(binding.ivIconPpl);
        resetIcon(binding.ivIconFullBody);
        resetIcon(binding.ivIconUpperLower);


        if (activePlan.equals("Push / Pull / Legs")) {
            binding.cardPlanPpl.setBackgroundResource(R.drawable.bg_active_plan_card);
            setActiveBadge(binding.tvBadgePpl);
            setActiveIcon(binding.ivIconPpl);

        } else if (activePlan.equals("Full Body Foundation")) {
            binding.cardPlanFullBody.setBackgroundResource(R.drawable.bg_active_plan_card);
            setActiveBadge(binding.tvBadgeFullBody);
            setActiveIcon(binding.ivIconFullBody);

        } else if (activePlan.equals("Upper / Lower Split")) {
            binding.cardPlanUpperLower.setBackgroundResource(R.drawable.bg_active_plan_card);
            setActiveBadge(binding.tvBadgeUpperLower);
            setActiveIcon(binding.ivIconUpperLower);
        }

        updateWeeklyRhythmCards(activePlan);
        updateMomentumCardUI(activePlan);
    }

    private void setActiveBadge(TextView textView) {
        textView.setText("• Active plan");
        textView.setTextColor(getResources().getColor(R.color.primary_orange, getTheme()));
        textView.setBackgroundResource(R.drawable.bg_icon_badge);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);
    }

    private void resetBadge(TextView textView, String defaultText) {
        textView.setText(defaultText);
        textView.setTextColor(getResources().getColor(R.color.text_muted, getTheme()));
        textView.setBackgroundResource(R.drawable.bg_outline_button);
        textView.setTypeface(null, android.graphics.Typeface.NORMAL);
    }

    private void setActiveIcon(ImageView imageView) {
        imageView.setBackgroundResource(R.drawable.bg_icon_badge);
        imageView.setImageTintList(android.content.res.ColorStateList.valueOf(
                getResources().getColor(R.color.primary_orange, getTheme())
        ));
    }

    private void resetIcon(ImageView imageView) {
        imageView.setBackgroundResource(R.drawable.bg_avatar_circle);
        imageView.setImageTintList(android.content.res.ColorStateList.valueOf(
                getResources().getColor(R.color.text_dark, getTheme())
        ));
    }

    private void updateWeeklyRhythmCards(String activePlan) {
        List<DayRhythmItem> rhythmList = new ArrayList<>();
        String todayName = getCurrentDayName();

        if (activePlan.equals("Push / Pull / Legs")) {
            rhythmList.add(new DayRhythmItem("MON", "Push day", "6 exercises · 48 min", R.drawable.ic_dumbbell, todayName.equals("MON"), false));
            rhythmList.add(new DayRhythmItem("TUE", "Pull day", "6 exercises · 52 min", R.drawable.ic_dumbbell, todayName.equals("TUE"), false));
            rhythmList.add(new DayRhythmItem("WED", "Legs day", "5 exercises · 45 min", R.drawable.ic_dumbbell, todayName.equals("WED"), false));
            rhythmList.add(new DayRhythmItem("THU", "Push day", "6 exercises · 48 min", R.drawable.ic_dumbbell, todayName.equals("THU"), false));
            rhythmList.add(new DayRhythmItem("FRI", "Pull day", "6 exercises · 52 min", R.drawable.ic_dumbbell, todayName.equals("FRI"), false));
            rhythmList.add(new DayRhythmItem("SAT", "Legs day", "5 exercises · 45 min", R.drawable.ic_dumbbell, todayName.equals("SAT"), false));
            rhythmList.add(new DayRhythmItem("SUN", "Rest day", "Active Recovery", R.drawable.ic_flame, todayName.equals("SUN"), true));

        } else if (activePlan.equals("Full Body Foundation")) {
            rhythmList.add(new DayRhythmItem("MON", "Full Body A", "7 exercises · 60 min", R.drawable.ic_dumbbell, todayName.equals("MON"), false));
            rhythmList.add(new DayRhythmItem("TUE", "Rest day", "Active Recovery", R.drawable.ic_flame, todayName.equals("TUE"), true));
            rhythmList.add(new DayRhythmItem("WED", "Full Body B", "7 exercises · 60 min", R.drawable.ic_dumbbell, todayName.equals("WED"), false));
            rhythmList.add(new DayRhythmItem("THU", "Rest day", "Active Recovery", R.drawable.ic_flame, todayName.equals("THU"), true));
            rhythmList.add(new DayRhythmItem("FRI", "Full Body C", "7 exercises · 60 min", R.drawable.ic_dumbbell, todayName.equals("FRI"), false));
            rhythmList.add(new DayRhythmItem("SAT", "Rest day", "Active Recovery", R.drawable.ic_flame, todayName.equals("SAT"), true));
            rhythmList.add(new DayRhythmItem("SUN", "Rest day", "Active Recovery", R.drawable.ic_flame, todayName.equals("SUN"), true));

        } else if (activePlan.equals("Upper / Lower Split")) {
            rhythmList.add(new DayRhythmItem("MON", "Upper Body", "6 exercises · 50 min", R.drawable.ic_dumbbell, todayName.equals("MON"), false));
            rhythmList.add(new DayRhythmItem("TUE", "Lower Body", "5 exercises · 55 min", R.drawable.ic_dumbbell, todayName.equals("TUE"), false));
            rhythmList.add(new DayRhythmItem("WED", "Rest day", "Active Recovery", R.drawable.ic_flame, todayName.equals("WED"), true));
            rhythmList.add(new DayRhythmItem("THU", "Upper Body", "6 exercises · 50 min", R.drawable.ic_dumbbell, todayName.equals("THU"), false));
            rhythmList.add(new DayRhythmItem("FRI", "Lower Body", "5 exercises · 55 min", R.drawable.ic_dumbbell, todayName.equals("FRI"), false));
            rhythmList.add(new DayRhythmItem("SAT", "Rest day", "Active Recovery", R.drawable.ic_flame, todayName.equals("SAT"), true));
            rhythmList.add(new DayRhythmItem("SUN", "Rest day", "Active Recovery", R.drawable.ic_flame, todayName.equals("SUN"), true));
        }

        if (weeklyAdapter != null) {
            weeklyAdapter.setItems(rhythmList);

            int todayIndex = getTodayPositionIndex();
            if (todayIndex != -1) {
                binding.rvWeeklyRhythm.scrollToPosition(todayIndex);
            }
        }
    }
    private void updateMomentumCardUI(String activePlan) {
        int targetWorkouts = 6;

        if (activePlan.equals("Push / Pull / Legs")) {
            targetWorkouts = 6;
        } else if (activePlan.equals("Full Body Foundation")) {
            targetWorkouts = 3;
        } else if (activePlan.equals("Upper / Lower Split")) {
            targetWorkouts = 4;
        }

        // Set dynamic plan target
        binding.tvWorkoutsTarget.setText(" / " + targetWorkouts);
        binding.pbMomentumWorkouts.setMax(targetWorkouts);

        // Fetch actual progress from ViewModel or Room DB
        viewModel.getTotalWorkoutsCount().observe(this, count -> {
            int completed = (count != null) ? count : 0;

            int maxTarget = binding.pbMomentumWorkouts.getMax();
            if (completed > maxTarget) {
                completed = maxTarget;
            }

            binding.tvWorkoutsCompleted.setText(String.valueOf(completed));
            binding.pbMomentumWorkouts.setProgress(completed);
        });

        viewModel.getTotalDurationMinutes().observe(this, minutes -> {
            if (minutes != null) {
                double hours = minutes / 60.0;
                binding.tvTimeInvested.setText(String.format(java.util.Locale.US, "%.1f hrs", hours));
            } else {
                binding.tvTimeInvested.setText("0 hrs");
            }
        });
    }
    private String getCurrentDayName() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case java.util.Calendar.TUESDAY:
                return "TUE";
            case java.util.Calendar.WEDNESDAY:
                return "WED";
            case java.util.Calendar.THURSDAY:
                return "THU";
            case java.util.Calendar.FRIDAY:
                return "FRI";
            case java.util.Calendar.SATURDAY:
                return "SAT";
            case java.util.Calendar.SUNDAY:
                return "SUN";
            case java.util.Calendar.MONDAY:
            default:
                return "MON";
        }
    }
    private void saveSelectedPlan(String planName) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_ACTIVE_PLAN, planName)
                .apply();
    }
    private String getSavedPlan() {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(KEY_ACTIVE_PLAN, "Push / Pull / Legs");
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}