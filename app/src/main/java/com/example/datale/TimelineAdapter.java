package com.example.datale;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private final ArrayList<Entries> diaryEntries;

    public TimelineAdapter(ArrayList<Entries>  diaryEntries) {
        this.diaryEntries = diaryEntries;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.textViewTitle.setText(diaryEntries.get(position).getEentry());
        viewHolder.textViewDate.setText(diaryEntries.get(position).getEdate());
        viewHolder.imageViewIcon.setImageDrawable(null);
        viewHolder.constraintLayout.setBackgroundColor(Color.parseColor("#3E50B4"));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.timeline_item, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return diaryEntries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewDate;
        private final ImageView imageViewIcon;
        private final ConstraintLayout constraintLayout;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.text_view_timeline_title);
            textViewDate = view.findViewById(R.id.text_view_timeline_date);
            imageViewIcon = view.findViewById(R.id.image_view_timeline_icon);
            constraintLayout = view.findViewById(R.id.constraint_layout_timeline);
        }
    }
}
