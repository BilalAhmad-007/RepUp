package com.bilalahmad.repup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.bilalahmad.repup.dialog.CustomizePlanBottomSheet;
import com.bilalahmad.repup.databinding.ItemCustomPlanCardBinding;
import com.bilalahmad.repup.dialog.StartWorkoutBottomSheet;
import com.bilalahmad.repup.ui.HistoryFragment;
import com.bilalahmad.repup.ui.MetricsFragment;
import com.bilalahmad.repup.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private String currentActivePlan = "Push / Pull / Legs";
    private static final String PREFS_NAME = "RepUpPrefs";
    private static final String KEY_ACTIVE_PLAN = "active_plan";
    private static final String KEY_CUSTOM_PLANS = "custom_plans_list";
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
        loadCustomPlanCardsUI();

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
            if (isTodayRestDay()) {
                new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                        .setTitle("Rest Day")
                        .setMessage("Today is a rest day. Enjoy your recovery and let your muscles grow!")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                StartWorkoutBottomSheet bottomSheet = StartWorkoutBottomSheet.newInstance(currentActivePlan);
                bottomSheet.show(getSupportFragmentManager(), "StartWorkoutBottomSheet");
            }
        });
        binding.btnCustomizePlan.setOnClickListener(v -> {
           openCustomizePlanBottomSheet();
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
    private void openCustomizePlanBottomSheet() {
        CustomizePlanBottomSheet bottomSheet = new CustomizePlanBottomSheet();
        bottomSheet.setOnPlanSavedListener(newPlanName -> {
            currentActivePlan = newPlanName;
            saveSelectedPlan(currentActivePlan);
            saveCustomPlanToPrefs(newPlanName);

            updatePlanCardsUI(currentActivePlan);
            loadCustomPlanCardsUI();

            Toast.makeText(this, "Custom plan '" + newPlanName + "' created & saved!", Toast.LENGTH_SHORT).show();
        });
        bottomSheet.show(getSupportFragmentManager(), "CustomizePlanBottomSheet");
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

        loadCustomPlanCardsUI();

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
        else {
            rhythmList = loadCustomWeeklyRhythm(activePlan);
        }

        if (weeklyAdapter != null) {
            weeklyAdapter.setItems(rhythmList);

            int todayIndex = getTodayPositionIndex();
            if (todayIndex != -1) {
                binding.rvWeeklyRhythm.scrollToPosition(todayIndex);
            }
        }
    }

    private List<DayRhythmItem> loadCustomWeeklyRhythm(String planName) {
        List<DayRhythmItem> rhythmList = new ArrayList<>();
        String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        String todayName = getCurrentDayName();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        for (String day : days) {
            String data = prefs.getString(planName + "_" + day, "");
            String dayTitle = prefs.getString(planName + "_" + day + "_title", "");
            if (data.isEmpty() && dayTitle.isEmpty()) {
                rhythmList.add(new DayRhythmItem(day, "Rest day", "Active Recovery", R.drawable.ic_flame, day.equals(todayName), true));
            } else {
                String[] exercises = data.isEmpty() ? new String[0] : data.split(";");
                int count = exercises.length;
                int estMinutes = count * 8;
                String subtitle = count + " exercises · " + estMinutes + " min";

                String titleToDisplay = dayTitle.isEmpty() ? "Custom Workout" : dayTitle;

                rhythmList.add(new DayRhythmItem(day, titleToDisplay, subtitle, R.drawable.ic_dumbbell, day.equals(todayName), false));
            }
        }
        return rhythmList;
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
        else {
            targetWorkouts = getCustomPlanWorkoutDaysCount(activePlan);
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

    private int getCustomPlanWorkoutDaysCount(String planName) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        int workoutDays = 0;
        for (String day : days) {
            if (!prefs.getString(planName + "_" + day, "").isEmpty()) {
                workoutDays++;
            }
        }
        return Math.max(workoutDays, 1);
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

    private void saveCustomPlanToPrefs(String planName) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> customPlans = new HashSet<>(prefs.getStringSet(KEY_CUSTOM_PLANS, new HashSet<>()));
        customPlans.add(planName);

        prefs.edit().putStringSet(KEY_CUSTOM_PLANS, customPlans).apply();
    }
    private List<String> getSavedCustomPlans() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> customPlans = new HashSet<>(prefs.getStringSet(KEY_CUSTOM_PLANS, new HashSet<>()));
        return new ArrayList<>(customPlans);
    }
    private void loadCustomPlanCardsUI() {
        binding.layoutCustomPlansContainer.removeAllViews();
        List<String> customPlans = getSavedCustomPlans();

        for (String planName : customPlans) {
            ItemCustomPlanCardBinding cardBinding = ItemCustomPlanCardBinding.inflate(
                    getLayoutInflater(), binding.layoutCustomPlansContainer, false
            );

            cardBinding.tvCustomPlanTitle.setText(planName);

            if (planName.equals(currentActivePlan)) {
                cardBinding.getRoot().setBackgroundResource(R.drawable.bg_active_plan_card);
                cardBinding.tvCustomBadge.setText("• Active plan");
                cardBinding.tvCustomBadge.setTextColor(getResources().getColor(R.color.primary_orange, getTheme()));
                cardBinding.tvCustomBadge.setBackgroundResource(R.drawable.bg_icon_badge);
                cardBinding.tvCustomBadge.setTypeface(null, android.graphics.Typeface.BOLD);

                cardBinding.ivCustomIcon.setBackgroundResource(R.drawable.bg_icon_badge);
                cardBinding.ivCustomIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
                        getResources().getColor(R.color.primary_orange, getTheme())
                ));

            } else {
                cardBinding.getRoot().setBackgroundResource(R.drawable.bg_white_card);
                cardBinding.tvCustomBadge.setText("Custom");
                cardBinding.tvCustomBadge.setTextColor(getResources().getColor(R.color.text_muted, getTheme()));
                cardBinding.tvCustomBadge.setBackgroundResource(R.drawable.bg_outline_button);
                cardBinding.tvCustomBadge.setTypeface(null, android.graphics.Typeface.NORMAL);

                cardBinding.ivCustomIcon.setBackgroundResource(R.drawable.bg_avatar_circle);
                cardBinding.ivCustomIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
                        getResources().getColor(R.color.text_dark, getTheme())
                ));
            }

            cardBinding.getRoot().setOnClickListener(v -> handlePlanClick(planName));

            cardBinding.getRoot().setOnLongClickListener(v -> {
                showDeleteCustomPlanDialog(planName);
                return true;
            });

            binding.layoutCustomPlansContainer.addView(cardBinding.getRoot());
        }
    }

    private void showDeleteCustomPlanDialog(String planName) {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("Delete Custom Plan?")
                .setMessage("Are you sure you want to delete '" + planName + "'? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteCustomPlanFromPrefs(planName);

                    if (planName.equals(currentActivePlan)) {
                        currentActivePlan = "Push / Pull / Legs";
                        saveSelectedPlan(currentActivePlan);
                        updatePlanCardsUI(currentActivePlan);
                    } else {
                        loadCustomPlanCardsUI();
                    }

                    Toast.makeText(this, "Custom plan deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteCustomPlanFromPrefs(String planName) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Set<String> customPlans = new HashSet<>(prefs.getStringSet(KEY_CUSTOM_PLANS, new HashSet<>()));
        customPlans.remove(planName);
        prefs.edit().putStringSet(KEY_CUSTOM_PLANS, customPlans).apply();

        SharedPreferences.Editor editor = prefs.edit();
        String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        for (String day : days) {
            editor.remove(planName + "_" + day);
        }
        editor.apply();
    }


    private boolean isTodayRestDay() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String activePlan = prefs.getString("ACTIVE_PLAN", "Push / Pull / Legs");

        // Get current day
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);
        String todayKey;
        switch (dayOfWeek) {
            case java.util.Calendar.TUESDAY: todayKey = "TUE"; break;
            case java.util.Calendar.WEDNESDAY: todayKey = "WED"; break;
            case java.util.Calendar.THURSDAY: todayKey = "THU"; break;
            case java.util.Calendar.FRIDAY: todayKey = "FRI"; break;
            case java.util.Calendar.SATURDAY: todayKey = "SAT"; break;
            case java.util.Calendar.SUNDAY: todayKey = "SUN"; break;
            case java.util.Calendar.MONDAY:
            default: todayKey = "MON"; break;
        }

        String exerciseData = "";
        if (activePlan.equals("Push / Pull / Legs")) {
            if (todayKey.equals("MON") || todayKey.equals("THU") ||
                    todayKey.equals("TUE") || todayKey.equals("FRI") ||
                    todayKey.equals("WED") || todayKey.equals("SAT")) {
                return false; // PPL has workouts on these days
            }
            return true; // Sunday is rest day
        } else if (activePlan.equals("Full Body Foundation")) {
            return !(todayKey.equals("MON") || todayKey.equals("WED") || todayKey.equals("FRI"));
        } else if (activePlan.equals("Upper / Lower Split")) {
            return !(todayKey.equals("MON") || todayKey.equals("TUE") || todayKey.equals("THU") || todayKey.equals("FRI"));
        } else {
            // Custom plan check
            exerciseData = prefs.getString(activePlan + "_" + todayKey, "");
            return exerciseData.isEmpty();
        }
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}