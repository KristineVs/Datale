package com.example.datale;

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

    private final ArrayList<TimelineDiaryEntry> diaryEntries;

    public TimelineAdapter(ArrayList<TimelineDiaryEntry>  diaryEntries) {
        this.diaryEntries = diaryEntries;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.textViewTitle.setText(diaryEntries.get(position).getTitle());
        viewHolder.textViewDate.setText(diaryEntries.get(position).getDate());
        viewHolder.imageViewIcon.setImageDrawable(diaryEntries.get(position).getIcon());
        viewHolder.constraintLayout.setBackgroundColor(diaryEntries.get(position).getBackgroundColor());
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

    public static class TimelineDiaryEntry {
        public String title;
        public String date;
        public Drawable icon;
        public int backgroundColor;

        public TimelineDiaryEntry(String title, String date, Drawable icon, int backgroundColor) {
            this.title = title;
            this.date = date;
            this.icon = icon;
            this.backgroundColor = backgroundColor;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public int getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
        }
    }
}
