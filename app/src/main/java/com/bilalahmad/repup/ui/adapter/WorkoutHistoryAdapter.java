package com.bilalahmad.repup.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bilalahmad.repup.data.entity.Workout;
import com.bilalahmad.repup.databinding.ItemWorkoutHistoryBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkoutHistoryAdapter extends ListAdapter<Workout, WorkoutHistoryAdapter.WorkoutViewHolder> {

    public WorkoutHistoryAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Workout> DIFF_CALLBACK = new DiffUtil.ItemCallback<Workout>() {
        @Override
        public boolean areItemsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.getWorkoutId() == newItem.getWorkoutId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.getTimestamp() == newItem.getTimestamp() &&
                    oldItem.getTitle().equals(newItem.getTitle());
        }
    };

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWorkoutHistoryBinding binding = ItemWorkoutHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkoutViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final ItemWorkoutHistoryBinding binding;

        public WorkoutViewHolder(@NonNull ItemWorkoutHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Workout workout) {
            binding.tvWorkoutTitle.setText(workout.getTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());
            String formattedDate = sdf.format(new Date(workout.getTimestamp()));
            binding.tvWorkoutDate.setText(formattedDate);
        }
    }
}