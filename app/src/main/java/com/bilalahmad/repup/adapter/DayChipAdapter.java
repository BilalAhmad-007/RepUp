package com.bilalahmad.repup.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bilalahmad.repup.R;
import com.bilalahmad.repup.databinding.ItemDaySelectorBinding;
import com.bilalahmad.repup.model.DayChipItem;
import java.util.List;

public class DayChipAdapter extends RecyclerView.Adapter<DayChipAdapter.DayChipViewHolder> {
    private List<DayChipItem> dayList;
    private OnDayClickListener listener;

    public interface OnDayClickListener {
        void onDayClick(int position);
    }

    public DayChipAdapter(List<DayChipItem> dayList, OnDayClickListener listener) {
        this.dayList = dayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DayChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDaySelectorBinding binding = ItemDaySelectorBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new DayChipViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DayChipViewHolder holder, int position) {
        DayChipItem item = dayList.get(position);
        holder.binding.tvChipDayName.setText(item.getDayName());

        if (item.isRest()) {
            holder.binding.tvChipCount.setText("Rest");
        } else {
            holder.binding.tvChipCount.setText(String.valueOf(item.getExerciseCount()));
        }

        // Selection styling
        if (item.isSelected()) {
            holder.binding.layoutDayRoot.setBackgroundResource(R.drawable.bg_day_chip_selected);
            holder.binding.tvChipDayName.setTextColor(Color.parseColor("#E65100")); // Orange highlight
            holder.binding.tvChipCount.setTextColor(Color.parseColor("#E65100"));
        } else {
            holder.binding.layoutDayRoot.setBackgroundResource(R.drawable.bg_day_chip_unselected);
            holder.binding.tvChipDayName.setTextColor(Color.parseColor("#757575"));
            holder.binding.tvChipCount.setTextColor(Color.parseColor("#757575"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onDayClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    static class DayChipViewHolder extends RecyclerView.ViewHolder {
        ItemDaySelectorBinding binding;
        public DayChipViewHolder(ItemDaySelectorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}