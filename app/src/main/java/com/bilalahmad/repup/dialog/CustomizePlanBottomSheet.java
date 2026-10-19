package com.bilalahmad.repup.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bilalahmad.repup.R;
import com.bilalahmad.repup.adapter.DayChipAdapter;
import com.bilalahmad.repup.databinding.DialogCustomizePlanBinding;
import com.bilalahmad.repup.databinding.ItemAddedExerciseBinding;
import com.bilalahmad.repup.model.DayChipItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class CustomizePlanBottomSheet extends BottomSheetDialogFragment {

    private DialogCustomizePlanBinding binding;
    private OnPlanSavedListener listener;

    private List<DayChipItem> dayList;
    private List<List<String>> weeklyAddedExercises;
    private DayChipAdapter dayChipAdapter;
    private AddedExerciseAdapter addedExerciseAdapter;
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
        setupSpinner();
        setupAddedExercisesRecyclerView(selectedDayIndex);

        binding.btnClose.setOnClickListener(v -> dismiss());
        binding.btnCancel.setOnClickListener(v -> dismiss());

        // Custom Exercise Add Dialog Trigger
        binding.btnAddExerciseBox.setOnClickListener(v -> showAddCustomExerciseDialog());

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
        dayList.add(new DayChipItem("Mon", 0, true, true));
        dayList.add(new DayChipItem("Tue", 0, true, false));
        dayList.add(new DayChipItem("Wed", 0, true, false));
        dayList.add(new DayChipItem("Thu", 0, true, false));
        dayList.add(new DayChipItem("Fri", 0, true, false));
        dayList.add(new DayChipItem("Sat", 0, true, false));
        dayList.add(new DayChipItem("Sun", 0, true, false));

        weeklyAddedExercises = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            List<String> exercises = new ArrayList<>();
            weeklyAddedExercises.add(exercises);
        }
    }

    private void setupDayChipsRecyclerView() {
        dayChipAdapter = new DayChipAdapter(dayList, position -> {
            dayList.get(selectedDayIndex).setSelected(false);
            selectedDayIndex = position;
            dayList.get(selectedDayIndex).setSelected(true);
            dayChipAdapter.notifyDataSetChanged();

            updateSelectedDayHeader();
            setupAddedExercisesRecyclerView(selectedDayIndex);
        });

        binding.rvDayChips.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvDayChips.setAdapter(dayChipAdapter);
    }

    private void setupSpinner() {
        String[] categories = {
                "Select by category",
                "--- Chest, shoulders & triceps ---",
                "Barbell bench press",
                "Incline dumbbell press",
                "Cable fly",
                "Tricep pushdown",
                "Overhead press",
                "Lateral raise",
                "--- Back & biceps ---",
                "Lat pulldown",
                "Barbell row",
                "Bicep curl",
                "--- Legs & core ---",
                "Squat",
                "Romanian deadlift",
                "Leg press",
                "Plank",
                "--- Cardio ---",
                "Treadmill run"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        binding.spinnerCategory.setAdapter(adapter);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (!selectedItem.startsWith("Select") && !selectedItem.startsWith("---")) {
                    addExerciseToCurrentDay(selectedItem);
                    binding.spinnerCategory.setSelection(0); // Reset to default prompt
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void addExerciseToCurrentDay(String exerciseName) {
        List<String> currentList = weeklyAddedExercises.get(selectedDayIndex);
        if (!currentList.contains(exerciseName)) {
            currentList.add(exerciseName);
            dayList.set(selectedDayIndex, new DayChipItem(
                    dayList.get(selectedDayIndex).getDayName(),
                    currentList.size(),
                    currentList.isEmpty(),
                    true
            ));
            dayChipAdapter.notifyItemChanged(selectedDayIndex);
            addedExerciseAdapter.notifyDataSetChanged();
            updateSelectedDayHeader();
        } else {
            Toast.makeText(getContext(), "Exercise already added", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddCustomExerciseDialog() {
        EditText input = new EditText(getContext());
        input.setHint("Enter custom exercise name");
        input.setPadding(40, 30, 40, 30);

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Add Custom Exercise")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String customName = input.getText().toString().trim();
                    if (!customName.isEmpty()) {
                        addExerciseToCurrentDay(customName);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupAddedExercisesRecyclerView(int dayIndex) {
        List<String> currentExercises = weeklyAddedExercises.get(dayIndex);
        addedExerciseAdapter = new AddedExerciseAdapter(currentExercises, position -> {
            currentExercises.remove(position);
            dayList.set(selectedDayIndex, new DayChipItem(
                    dayList.get(selectedDayIndex).getDayName(),
                    currentExercises.size(),
                    currentExercises.isEmpty(),
                    true
            ));
            dayChipAdapter.notifyItemChanged(selectedDayIndex);
            addedExerciseAdapter.notifyDataSetChanged();
            updateSelectedDayHeader();
        });

        binding.rvAddedExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAddedExercises.setAdapter(addedExerciseAdapter);
        updateSelectedDayHeader();
    }

    private void updateSelectedDayHeader() {
        String[] fullDayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        binding.tvSelectedDayTitle.setText(fullDayNames[selectedDayIndex]);

        int count = weeklyAddedExercises.get(selectedDayIndex).size();
        binding.tvSelectedExerciseCount.setText(count + " exercises selected");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Inner Adapter for Added Exercises List
    private static class AddedExerciseAdapter extends RecyclerView.Adapter<AddedExerciseAdapter.ViewHolder> {
        private final List<String> exercises;
        private final OnDeleteListener deleteListener;

        public interface OnDeleteListener {
            void onDelete(int position);
        }

        public AddedExerciseAdapter(List<String> exercises, OnDeleteListener deleteListener) {
            this.exercises = exercises;
            this.deleteListener = deleteListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemAddedExerciseBinding binding = ItemAddedExerciseBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.tvIndexBadge.setText(String.valueOf(position + 1));
            holder.binding.tvAddedExerciseName.setText(exercises.get(position));
            holder.binding.btnDeleteExercise.setOnClickListener(v -> {
                if (deleteListener != null) deleteListener.onDelete(position);
            });
        }

        @Override
        public int getItemCount() {
            return exercises.size();
        }



        static class ViewHolder extends RecyclerView.ViewHolder {
            ItemAddedExerciseBinding binding;
            public ViewHolder(ItemAddedExerciseBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}