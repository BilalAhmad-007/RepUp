package com.bilalahmad.repup.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bilalahmad.repup.DayRhythmItem;
import com.bilalahmad.repup.R;
import com.bilalahmad.repup.databinding.ItemDayRhythmBinding;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class WeeklyRhythmAdapter extends RecyclerView.Adapter<WeeklyRhythmAdapter.RhythmViewHolder>{
    private List<DayRhythmItem> items = new ArrayList<>();

    public void setItems(List<DayRhythmItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RhythmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDayRhythmBinding binding = ItemDayRhythmBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new RhythmViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RhythmViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RhythmViewHolder extends RecyclerView.ViewHolder {
        private final ItemDayRhythmBinding binding;

        public RhythmViewHolder(@NonNull ItemDayRhythmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DayRhythmItem item) {
            Context context = itemView.getContext();

            binding.tvDayName.setText(item.getDayName());
            binding.tvWorkoutTitle.setText(item.getWorkoutTitle());
            binding.tvSubText.setText(item.getSubText());
            binding.ivWorkoutIcon.setImageResource(item.getIconResId());

            int orangeColor = ContextCompat.getColor(context, R.color.primary_orange);
            int mutedColor = ContextCompat.getColor(context, R.color.text_muted);
            int darkColor = ContextCompat.getColor(context, R.color.text_dark);

            if (item.isSelected()) {
                //SELECTED CARD
                binding.cardRhythmRoot.setBackgroundResource(R.drawable.bg_orange_tint_card);
                binding.tvBadgeSelected.setVisibility(View.VISIBLE);
                binding.tvDayName.setTextColor(orangeColor);
                binding.ivWorkoutIcon.setImageTintList(ColorStateList.valueOf(orangeColor));
                binding.dividerLine.setBackgroundColor(Color.parseColor("#F2D2C6"));
                binding.layoutBottomStatus.setVisibility(View.VISIBLE);
                binding.tvActionText.setText("Let's get to work");
                binding.tvActionText.setTextColor(orangeColor);
                binding.tvActionArrow.setTextColor(orangeColor);

            } else if (item.isRestDay()) {
                //REST DAY CARD
                binding.cardRhythmRoot.setBackgroundResource(R.drawable.bg_muted_green_card);
                binding.tvBadgeSelected.setVisibility(View.GONE);
                binding.tvDayName.setTextColor(mutedColor);
                binding.ivWorkoutIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pulse));
                binding.ivWorkoutIcon.setImageTintList(ColorStateList.valueOf(mutedColor));
                binding.dividerLine.setBackgroundColor(Color.parseColor("#5513150E"));
                binding.layoutBottomStatus.setVisibility(View.VISIBLE);
                binding.tvActionText.setText("You've earned it");
                binding.tvActionText.setTextColor(mutedColor);
                binding.tvActionArrow.setVisibility(View.GONE);

            } else {
                //UNSELECTED CARD
                binding.cardRhythmRoot.setBackgroundResource(R.drawable.bg_white_card);
                binding.tvBadgeSelected.setVisibility(View.GONE);
                binding.tvDayName.setTextColor(mutedColor);
                binding.ivWorkoutIcon.setImageTintList(ColorStateList.valueOf(darkColor));
                binding.dividerLine.setBackgroundColor(Color.parseColor("#E0E0E0"));
                binding.layoutBottomStatus.setVisibility(View.VISIBLE);
                binding.tvActionText.setText("Keep it going");
                binding.tvActionText.setTextColor(mutedColor);
                binding.tvActionArrow.setVisibility(View.GONE);
                binding.tvActionArrow.setTextColor(mutedColor);
            }
        }
    }
}
