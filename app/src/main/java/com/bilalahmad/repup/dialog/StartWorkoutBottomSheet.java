package com.bilalahmad.repup.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bilalahmad.repup.databinding.DialogStartWorkoutBinding;
import com.bilalahmad.repup.databinding.ItemWorkoutExerciseBinding;
import com.bilalahmad.repup.databinding.ItemWorkoutSetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StartWorkoutBottomSheet extends BottomSheetDialogFragment {

    private DialogStartWorkoutBinding binding;
    private String activePlan = "Push / Pull / Legs";
    public static StartWorkoutBottomSheet newInstance(String planName) {
        StartWorkoutBottomSheet fragment = new StartWorkoutBottomSheet();
        Bundle args = new Bundle();
        args.putString("ACTIVE_PLAN_KEY", planName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogStartWorkoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnClose.setOnClickListener(v -> dismiss());

        if (getArguments() != null) {
            activePlan = getArguments().getString("ACTIVE_PLAN_KEY", "Push / Pull / Legs");
        }

        List<ExerciseItem> exerciseList = loadActiveDayExercises();

        // Fetch custom day title if available, otherwise fallback to "MON Workout"
        SharedPreferences prefs = requireContext().getSharedPreferences("RepUpPrefs", Context.MODE_PRIVATE);
        String todayKey = getCurrentDayName();
        String customTitle = prefs.getString(activePlan + "_" + todayKey + "_title", "");

        if (!customTitle.isEmpty()) {
            binding.tvWorkoutTitle.setText(customTitle + ". Let’s do this.");
        } else {
            binding.tvWorkoutTitle.setText(todayKey + " workout. Let’s do this.");
        }

        binding.tvExercisesRecorded.setText("0 / " + exerciseList.size() + " exercises recorded");

        WorkoutExerciseAdapter adapter = new WorkoutExerciseAdapter(exerciseList);
        binding.rvWorkoutExercises.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvWorkoutExercises.setAdapter(adapter);

        binding.btnSaveWorkout.setOnClickListener(v -> {
            // TODO: Save workout session logic
            dismiss();
        });
    }

    private List<ExerciseItem> loadActiveDayExercises() {
        List<ExerciseItem> exerciseItems = new ArrayList<>();
        Context context = getContext();
        if (context == null) return exerciseItems;

        SharedPreferences prefs = context.getSharedPreferences("RepUpPrefs", Context.MODE_PRIVATE);
        String todayKey = getCurrentDayName();

        String exerciseData = "";

        // REMOVED local 'String activePlan = ...' line so it uses the passed activePlan!
        if (activePlan.equals("Push / Pull / Legs")) {
            if (!todayKey.equals("SUN")) {
                if (todayKey.equals("MON") || todayKey.equals("THU")) {
                    exerciseData = "Barbell bench press;Incline dumbbell press;Overhead press;Lateral raise;Triceps pushdown";
                } else if (todayKey.equals("TUE") || todayKey.equals("FRI")) {
                    exerciseData = "Pull-up;Barbell row;Lat pulldown;Face pull;Dumbbell curl";
                } else if (todayKey.equals("WED") || todayKey.equals("SAT")) {
                    exerciseData = "Barbell squat;Leg press;Romanian deadlift;Leg extension;Calf raise";
                }
            }
        } else if (activePlan.equals("Full Body Foundation")) {
            if (todayKey.equals("MON") || todayKey.equals("WED") || todayKey.equals("FRI")) {
                exerciseData = "Goblet squat;Push-up;Dumbbell row;Plank;Lunges";
            }
        } else if (activePlan.equals("Upper / Lower Split")) {
            if (todayKey.equals("MON") || todayKey.equals("THU")) {
                exerciseData = "Bench press;Overhead press;Barbell row;Lat pulldown";
            } else if (todayKey.equals("TUE") || todayKey.equals("FRI")) {
                exerciseData = "Squat;Deadlift;Leg curl;Calf raise";
            }
        } else {
            // Custom plan exercises lookup from SharedPreferences
            exerciseData = prefs.getString(activePlan + "_" + todayKey, "");
        }
//Java 1.110
        if (!exerciseData.isEmpty()) {
            String[] exercises = exerciseData.split(";");
            int index = 1;
            for (String exName : exercises) {
                if (!exName.trim().isEmpty()) {
                    String formattedNum = String.format(Locale.US, "%02d", index++);
                    exerciseItems.add(new ExerciseItem(formattedNum, exName.trim(), "Suggested: 3 sets × 10 reps · 60 sec rest"));
                }
            }
        }

        return exerciseItems;
    }

    private String getCurrentDayName() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case java.util.Calendar.TUESDAY: return "TUE";
            case java.util.Calendar.WEDNESDAY: return "WED";
            case java.util.Calendar.THURSDAY: return "THU";
            case java.util.Calendar.FRIDAY: return "FRI";
            case java.util.Calendar.SATURDAY: return "SAT";
            case java.util.Calendar.SUNDAY: return "SUN";
            case java.util.Calendar.MONDAY:
            default: return "MON";
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Inner Models and Adapter
    public static class ExerciseItem {
        String number;
        String name;
        String suggested;
        List<String> sets;

        public ExerciseItem(String number, String name, String suggested) {
            this.number = number;
            this.name = name;
            this.suggested = suggested;
            this.sets = new ArrayList<>();
            this.sets.add("");
            this.sets.add("");
            this.sets.add("");
        }
    }

    private static class WorkoutExerciseAdapter extends RecyclerView.Adapter<WorkoutExerciseAdapter.ViewHolder> {
        private final List<ExerciseItem> items;

        public WorkoutExerciseAdapter(List<ExerciseItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemWorkoutExerciseBinding binding = ItemWorkoutExerciseBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ExerciseItem item = items.get(position);
            holder.binding.tvExerciseNumber.setText(item.number);
            holder.binding.tvExerciseName.setText(item.name);
            holder.binding.tvExerciseSuggested.setText(item.suggested);

            holder.binding.layoutSetsContainer.removeAllViews();
            for (int i = 0; i < item.sets.size(); i++) {
                final int setIndex = i;
                ItemWorkoutSetBinding setBinding = ItemWorkoutSetBinding.inflate(
                        LayoutInflater.from(holder.itemView.getContext()), holder.binding.layoutSetsContainer, false
                );

                setBinding.tvSetNumber.setText(String.format(Locale.US, "%02d", (i + 1)));
                setBinding.etActualReps.setText(item.sets.get(i));

                setBinding.btnDeleteSet.setOnClickListener(v -> {
                    if (item.sets.size() > 1) {
                        item.sets.remove(setIndex);
                        notifyItemChanged(position);
                    }
                });

                holder.binding.layoutSetsContainer.addView(setBinding.getRoot());
            }

            holder.binding.btnAddSet.setOnClickListener(v -> {
                item.sets.add("");
                notifyItemChanged(position);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ItemWorkoutExerciseBinding binding;

            public ViewHolder(ItemWorkoutExerciseBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }


}