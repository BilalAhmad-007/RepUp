package com.bilalahmad.repup.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bilalahmad.repup.R;
import com.bilalahmad.repup.databinding.ActivityActiveWorkoutBinding;
import com.bilalahmad.repup.ui.viewmodel.WorkoutViewModel;

public class ActiveWorkoutActivity extends AppCompatActivity {
    private ActivityActiveWorkoutBinding binding;
    private WorkoutViewModel viewModel;
    private int currentSetNumber =1;
        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            binding = DataBindingUtil.setContentView(this, R.layout.activity_active_workout);
            viewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

            //Attaching viewModel to data binding
            binding.setViewModel(viewModel);
            binding.setLifecycleOwner(this);

            //sample push day session
            viewModel.startNewWorkout("Push Day");

            //click listener to log set entries
            binding.btnLogSet.setOnClickListener(v -> {
                 String weightStr = binding.etWeight.getText()!=null?binding.etWeight.getText().toString():"";
                 String repsStr = binding.etReps.getText()!=null?binding.etReps.getText().toString():"";

                 if(!TextUtils.isEmpty(weightStr) && !TextUtils.isEmpty(repsStr)){
                     double weight = Double.parseDouble(weightStr);
                     int reps = Integer.parseInt(repsStr);

                     viewModel.logSet(1, currentSetNumber, weight,reps);
                     Toast.makeText(this, "Set "+currentSetNumber+" Logged!", Toast.LENGTH_SHORT).show();
                     currentSetNumber++;
                     binding.etWeight.setText("");
                     binding.etReps.setText("");
                 }else{
                     Toast.makeText(this, "Please enter weight and reps", Toast.LENGTH_SHORT).show();
                 }
            });
        }
}