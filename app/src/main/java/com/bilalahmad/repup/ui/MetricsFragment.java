package com.bilalahmad.repup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bilalahmad.repup.R;
import com.bilalahmad.repup.databinding.FragmentMetricsBinding;

import java.util.Locale;

public class MetricsFragment extends Fragment {

    private FragmentMetricsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMetricsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupSpinners();

        binding.btnCalculateMetrics.setOnClickListener(v -> calculateMetrics());
    }

    private void setupSpinners() {
        // Sex Spinner Options
        String[] sexOptions = {"Male", "Female"};
        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_spinner,
                sexOptions
        );
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSex.setAdapter(sexAdapter);

        // Activity Level Spinner Options
        String[] activityOptions = {
                "Sedentary · Little to no exercise",
                "Lightly active · 1–3 days / week",
                "Moderately active · 3–5 days / week",
                "Very active · 6–7 days / week",
                "Extra active · Heavy physical training"
        };
        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_spinner,
                activityOptions
        );
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerActivity.setAdapter(activityAdapter);
        binding.spinnerActivity.setSelection(2);
    }

    private void calculateMetrics() {
        String ageStr = binding.etAge.getText().toString().trim();
        String heightStr = binding.etHeight.getText().toString().trim();
        String weightStr = binding.etWeight.getText().toString().trim();

        if (ageStr.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        double heightCm = Double.parseDouble(heightStr);
        double weightKg = Double.parseDouble(weightStr);
        boolean isMale = binding.spinnerSex.getSelectedItemPosition() == 0;

        // 1. BMI Calculation
        double heightM = heightCm / 100.0;
        double bmi = weightKg / (heightM * heightM);

        String bmiCategory;
        int textColor;

        if (bmi < 18.5) {
            bmiCategory = "Underweight";
            textColor = android.graphics.Color.parseColor("#D97706"); // Amber
        } else if (bmi < 25.0) {
            bmiCategory = "Healthy weight range";
            textColor = android.graphics.Color.parseColor("#2E7D32"); // Soft Green
        } else if (bmi < 30.0) {
            bmiCategory = "Overweight range";
            textColor = android.graphics.Color.parseColor("#FF6B00"); // Primary Orange
        } else {
            bmiCategory = "Obesity range";
            textColor = android.graphics.Color.parseColor("#DC2626"); // Crimson Red
        }

        // 2. BMR Calculation (Mifflin-St Jeor)
        double bmr = (10.0 * weightKg) + (6.25 * heightCm) - (5.0 * age) + (isMale ? 5 : -161);

        // 3. TDEE Calculation
        double[] multipliers = {1.2, 1.375, 1.55, 1.725, 1.9};
        double tdee = bmr * multipliers[binding.spinnerActivity.getSelectedItemPosition()];

        // Bind Values to UI
        binding.tvBmiValue.setText(String.format(Locale.US, "%.1f", bmi));
        binding.tvBmiCategory.setText(bmiCategory);
        binding.tvBmiCategory.setTextColor(textColor);

        binding.tvBmrValue.setText(String.format(Locale.US, "%,d", Math.round(bmr)));
        binding.tvTdeeValue.setText(String.format(Locale.US, "%,d", Math.round(tdee)));

        // Toggle visibility to display calculated results
        binding.layoutEmptyState.setVisibility(View.GONE);
        binding.layoutResultsState.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}