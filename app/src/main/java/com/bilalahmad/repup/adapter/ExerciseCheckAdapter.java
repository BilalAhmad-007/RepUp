package com.bilalahmad.repup.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bilalahmad.repup.databinding.ItemCustomizeExerciseBinding;
import com.bilalahmad.repup.model.ExerciseItem;
import java.util.List;

public class ExerciseCheckAdapter extends RecyclerView.Adapter<ExerciseCheckAdapter.ExerciseViewHolder> {
    private List<ExerciseItem> exerciseList;

    public ExerciseCheckAdapter(List<ExerciseItem> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCustomizeExerciseBinding binding = ItemCustomizeExerciseBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ExerciseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        ExerciseItem item = exerciseList.get(position);
        holder.binding.tvExerciseName.setText(item.getExerciseName());
        holder.binding.tvSetsReps.setText(item.getSetsReps());

        // Remove listener temporarily to avoid infinite loops during recycling
        holder.binding.cbExerciseSelect.setOnCheckedChangeListener(null);
        holder.binding.cbExerciseSelect.setChecked(item.isChecked());

        holder.binding.cbExerciseSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        ItemCustomizeExerciseBinding binding;
        public ExerciseViewHolder(ItemCustomizeExerciseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}