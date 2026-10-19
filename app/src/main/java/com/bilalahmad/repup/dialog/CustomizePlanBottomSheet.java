package com.bilalahmad.repup.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bilalahmad.repup.adapter.DayChipAdapter;
import com.bilalahmad.repup.adapter.ExerciseCheckAdapter;
import com.bilalahmad.repup.databinding.DialogCustomizePlanBinding;
import com.bilalahmad.repup.model.DayChipItem;
import com.bilalahmad.repup.model.ExerciseItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;

public class CustomizePlanBottomSheet extends BottomSheetDialogFragment {

    private DialogCustomizePlanBinding binding;
    private OnPlanSavedListener listener;

    private List<DayChipItem> dayList;
    private List<List<ExerciseItem>> weeklyExercises;
    private DayChipAdapter dayChipAdapter;
    private ExerciseCheckAdapter exerciseCheckAdapter;
    private int selectedDayIndex = 0;

    public interface OnPlanSavedListener {
        void onPlanSaved(String planName);
    }

    public void setOnPlanSavedListener(OnPlanSavedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogCustomizePlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initMockData();
        setupDayChipsRecyclerView();
        setupExercisesRecyclerView(selectedDayIndex);

        binding.btnClose.setOnClickListener(v -> dismiss());
        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.btnSavePlan.setOnClickListener(v -> {
            String newPlanName = binding.etPlanName.getText().toString().trim();
            if (!newPlanName.isEmpty() && listener != null) {
                listener.onPlanSaved(newPlanName);
            }
            dismiss();
        });
    }

    private void initMockData() {
        dayList = new ArrayList<>();
        dayList.add(new DayChipItem("Mon", 6, false, true));
        dayList.add(new DayChipItem("Tue", 3, false, false));
        dayList.add(new DayChipItem("Wed", 4, false, false));
        dayList.add(new DayChipItem("Thu", 6, false, false));
        dayList.add(new DayChipItem("Fri", 3, false, false));
        dayList.add(new DayChipItem("Sat", 4, false, false));
        dayList.add(new DayChipItem("Sun", 0, true, false));

        weeklyExercises = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            List<ExerciseItem> exercises = new ArrayList<>();
            exercises.add(new ExerciseItem("Barbell bench press", "3 sets × 10 reps", true));
            exercises.add(new ExerciseItem("Incline dumbbell press", "3 sets × 10 reps", true));
            exercises.add(new ExerciseItem("Cable fly", "3 sets × 10 reps", true));
            exercises.add(new ExerciseItem("Tricep pushdown", "3 sets × 10 reps", true));
            exercises.add(new ExerciseItem("Overhead press", "3 sets × 10 reps", true));
            exercises.add(new ExerciseItem("Lateral raise", "3 sets × 10 reps", true));
            weeklyExercises.add(exercises);
        }
    }

    private void setupDayChipsRecyclerView() {
        dayChipAdapter = new DayChipAdapter(dayList, position -> {
            dayList.get(selectedDayIndex).setSelected(false);
            selectedDayIndex = position;
            dayList.get(selectedDayIndex).setSelected(true);
            dayChipAdapter.notifyDataSetChanged();

            updateSelectedDayHeader();
            setupExercisesRecyclerView(selectedDayIndex);
        });

        binding.rvDayChips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvDayChips.setAdapter(dayChipAdapter);
    }

    private void setupExercisesRecyclerView(int dayIndex) {
        List<ExerciseItem> currentExercises = weeklyExercises.get(dayIndex);
        exerciseCheckAdapter = new ExerciseCheckAdapter(currentExercises);
        binding.rvExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvExercises.setAdapter(exerciseCheckAdapter);

        updateSelectedDayHeader();
    }

    private void updateSelectedDayHeader() {
        String[] fullDayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        binding.tvSelectedDayTitle.setText(fullDayNames[selectedDayIndex]);

        int count = 0;
        for (ExerciseItem item : weeklyExercises.get(selectedDayIndex)) {
            if (item.isChecked()) count++;
        }
        binding.tvSelectedExerciseCount.setText(count + " exercises selected");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}