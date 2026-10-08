package com.bilalahmad.repup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bilalahmad.repup.R;
import com.bilalahmad.repup.adapter.WorkoutAdapter;
import com.bilalahmad.repup.databinding.FragmentHistoryBinding;
import com.bilalahmad.repup.viewmodel.MainViewModel;

import java.util.Locale;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private WorkoutAdapter adapter;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        setupFilterSpinner();
        setupRecyclerView();
        observeViewModel();

        return binding.getRoot();
    }

    private void setupFilterSpinner() {
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.workout_filter_options, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFilter.setAdapter(filterAdapter);
    }

    private void setupRecyclerView() {
        binding.rvWorkoutJournal.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new WorkoutAdapter();
        binding.rvWorkoutJournal.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        viewModel.getAllWorkouts().observe(getViewLifecycleOwner(), workouts -> {
            if (workouts != null) {
                adapter.setWorkouts(workouts);
            }
        });

        viewModel.getTotalWorkoutsCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.tvTotalWorkoutsCount.setText(String.valueOf(count));
            }
        });

        viewModel.getTotalDurationMinutes().observe(getViewLifecycleOwner(), minutes -> {
            if (minutes != null) {
                double hours = minutes / 60.0;
                binding.tvTotalTimeInvested.setText(String.format(Locale.US, "%.1f", hours));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
