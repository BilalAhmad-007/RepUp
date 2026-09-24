package com.bilalahmad.repup.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bilalahmad.repup.databinding.FragmentHistoryBinding;
import com.bilalahmad.repup.ui.adapter.WorkoutHistoryAdapter;
import com.bilalahmad.repup.ui.viewmodel.WorkoutViewModel;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private WorkoutViewModel viewModel;
    private WorkoutHistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new WorkoutHistoryAdapter();
        binding.rvWorkoutHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvWorkoutHistory.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        viewModel.getAllWorkouts().observe(getViewLifecycleOwner(), workouts -> {
            if (workouts != null && !workouts.isEmpty()) {

                binding.tvEmptyState.setVisibility(View.GONE);
                binding.rvWorkoutHistory.setVisibility(View.VISIBLE);
                adapter.submitList(workouts);
            } else {
                binding.tvEmptyState.setVisibility(View.VISIBLE);
                binding.rvWorkoutHistory.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}