package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SlideshowPagerAdapter extends RecyclerView.Adapter<SlideshowPagerAdapter.ViewHolder> {

    private int[] imageIds = {
            R.drawable.alu,
            R.drawable.kola,
            R.drawable.begun
    };

    public SlideshowPagerAdapter(int[] imageIds) {
        this.imageIds = imageIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideshow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int imageId = imageIds[position];
        holder.imageView.setImageResource(imageId);
    }

    @Override
    public int getItemCount() {
        return imageIds.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
