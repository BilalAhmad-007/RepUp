package com.bilalahmad.repup.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bilalahmad.repup.databinding.FragmentActiveWorkoutBinding;
import com.bilalahmad.repup.ui.viewmodel.WorkoutViewModel;

public class ActiveWorkoutFragment extends Fragment {
    private FragmentActiveWorkoutBinding binding;
    private WorkoutViewModel viewModel;
    private int currentSetNumber = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentActiveWorkoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel.startNewWorkout("Push Day");

        binding.btnLogSet.setOnClickListener(v -> {
            String weightStr = binding.etWeight.getText() != null ? binding.etWeight.getText().toString() : "";
            String repsStr = binding.etReps.getText() != null ? binding.etReps.getText().toString() : "";

            if (!TextUtils.isEmpty(weightStr) && !TextUtils.isEmpty(repsStr)) {
                double weight = Double.parseDouble(weightStr);
                int reps = Integer.parseInt(repsStr);

                viewModel.logSet(1, currentSetNumber, weight, reps);

                Toast.makeText(requireContext(), "Set " + currentSetNumber + " logged!", Toast.LENGTH_SHORT).show();
                currentSetNumber++;

                binding.etWeight.setText("");
                binding.etReps.setText("");
            } else {
                Toast.makeText(requireContext(), "Please enter weight and reps", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}