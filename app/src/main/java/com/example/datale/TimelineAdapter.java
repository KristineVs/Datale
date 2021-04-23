package com.example.datale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private final ArrayList<Entries> diaryEntries;
    private Context context;

    public TimelineAdapter(Context context, ArrayList<Entries>  diaryEntries) {
        this.context = context;
        this.diaryEntries = diaryEntries;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.textViewTitle.setText(diaryEntries.get(position).getEtitle());

        Date currentDate = diaryEntries.get(position).getEdate();
        String monthName = DateFormat.format("MMMM", currentDate).toString();
        String day = DateFormat.format("d", currentDate).toString();
        String year = DateFormat.format("yyyy", currentDate).toString();
        String date = monthName + " " + day + ", " + year;
        viewHolder.textViewDate.setText(date);
        viewHolder.imageViewIcon.setImageDrawable(null);
        viewHolder.constraintLayout.setBackgroundColor(Color.parseColor("#3E50B4"));

        viewHolder.cardViewTimelineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent diaryEntryIntent = new Intent(context, DiaryActivity.class);
                diaryEntryIntent.putExtra("whichEntry", position);
                context.startActivity(diaryEntryIntent);
            }
        });
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
        private final CardView cardViewTimelineView;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.text_view_timeline_title);
            textViewDate = view.findViewById(R.id.text_view_timeline_date);
            imageViewIcon = view.findViewById(R.id.image_view_timeline_icon);
            constraintLayout = view.findViewById(R.id.constraint_layout_timeline);
            cardViewTimelineView = view.findViewById(R.id.card_view_timeline_item);
        }
    }
}
