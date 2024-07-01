package com.example.dietmate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietmate.R;

import java.util.List;

public class ProfileOptionsAdapter extends RecyclerView.Adapter<ProfileOptionsAdapter.ViewHolder> {

    private final List<Integer> optionImages;
    private final List<String> optionLabels;
    private final OnClickListener onClickListener;

    public ProfileOptionsAdapter(List<Integer> optionImages, List<String> optionLabels, OnClickListener onClickListener) {
        this.optionImages = optionImages;
        this.optionLabels = optionLabels;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.optionImage.setImageResource(optionImages.get(position));
        holder.optionLabel.setText(optionLabels.get(position));
        holder.option.setOnClickListener(v -> onClickListener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return optionImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View option;
        ImageView optionImage;
        TextView optionLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            option = itemView;
            optionImage = itemView.findViewById(R.id.option_image);
            optionLabel = itemView.findViewById(R.id.option_label);
        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}
