package com.bilalahmad.repup.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bilalahmad.repup.R;
import com.bilalahmad.repup.database.WorkoutSession;
import com.bilalahmad.repup.databinding.ItemWorkoutJournalBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>{
    private List<WorkoutSession> workoutList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWorkoutJournalBinding binding = ItemWorkoutJournalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkoutViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.bind(workoutList.get(position));
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public void setWorkouts(List<WorkoutSession> workouts) {
        this.workoutList = workouts;
        notifyDataSetChanged();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final ItemWorkoutJournalBinding binding;

        public WorkoutViewHolder(@NonNull ItemWorkoutJournalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WorkoutSession session) {
            binding.tvWorkoutTitle.setText(session.getWorkoutTitle());
            binding.tvWorkoutDate.setText(dateFormat.format(new Date(session.getTimestamp())));
            binding.tvDuration.setText("Duration · " + session.getDurationMinutes() + " min");
            binding.tvExerciseCount.setText(session.getExerciseCount() + " exercises");
        }
    }

}
