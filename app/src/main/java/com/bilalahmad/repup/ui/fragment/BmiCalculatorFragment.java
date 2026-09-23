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

import com.bilalahmad.repup.databinding.ActivityBmiCalculatorBinding;
import com.bilalahmad.repup.utils.HealthCalculator;

public class BmiCalculatorFragment extends Fragment {

    private ActivityBmiCalculatorBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityBmiCalculatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCalculateBmi.setOnClickListener(v -> {
            String weightStr = binding.etWeight.getText() != null ? binding.etWeight.getText().toString() : "";
            String heightStr = binding.etHeight.getText() != null ? binding.etHeight.getText().toString() : "";

            if (!TextUtils.isEmpty(weightStr) && !TextUtils.isEmpty(heightStr)) {
                double weight = Double.parseDouble(weightStr);
                double height = Double.parseDouble(heightStr);

                double bmi = HealthCalculator.calculateBMI(weight, height);
                String category = HealthCalculator.getBMICategory(bmi);

                binding.tvBmiScore.setText(String.valueOf(bmi));
                binding.tvBmiCategory.setText(category);
                binding.cardResult.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(requireContext(), "Please enter weight and height", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}