package com.bilalahmad.repup.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bilalahmad.repup.data.entity.SetRecord;
import com.bilalahmad.repup.databinding.ItemSetRecordBinding;

public class SetAdapter extends ListAdapter<SetRecord, SetAdapter.SetViewHolder> {

    public SetAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<SetRecord> DIFF_CALLBACK = new DiffUtil.ItemCallback<SetRecord>() {
        @Override
        public boolean areItemsTheSame(@NonNull SetRecord oldItem, @NonNull SetRecord newItem) {
            return oldItem.getSetRecordId() == newItem.getSetRecordId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SetRecord oldItem, @NonNull SetRecord newItem) {
            return oldItem.getWeight() == newItem.getWeight() &&
                    oldItem.getReps() == newItem.getReps() &&
                    oldItem.getSetNumber() == newItem.getSetNumber();
        }
    };

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSetRecordBinding binding = ItemSetRecordBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new SetViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class SetViewHolder extends RecyclerView.ViewHolder {
        private final ItemSetRecordBinding binding;

        public SetViewHolder(@NonNull ItemSetRecordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SetRecord set) {
            binding.tvSetNumber.setText("Set " + set.getSetNumber());
            binding.tvWeightAndReps.setText(set.getWeight() + " kg × " + set.getReps() + " reps");
        }
    }
}