package com.example.datale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.sql.Time;
import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ArrayList<TimelineAdapter.TimelineDiaryEntry> entries = new ArrayList<>();
        entries.add(new TimelineAdapter.TimelineDiaryEntry("Crazy day", "Feb 20, 2020", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_calendar, null), Color.parseColor("#3E50B4")));
        entries.add(new TimelineAdapter.TimelineDiaryEntry("Wandering in paris", "Feb 28, 2020", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_calendar, null), Color.parseColor("#3E50B4")));
        entries.add(new TimelineAdapter.TimelineDiaryEntry("Girls trip", "Mar 20, 2020", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_calendar, null), Color.parseColor("#B43E93")));
        entries.add(new TimelineAdapter.TimelineDiaryEntry("Want to go back", "Apr 4, 2020", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_calendar, null), Color.parseColor("#3E50B4")));
        entries.add(new TimelineAdapter.TimelineDiaryEntry("Kayaking", "Apr 20, 2020", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_calendar, null), Color.parseColor("#3E50B4")));
        entries.add(new TimelineAdapter.TimelineDiaryEntry("What am I doing", "May 16, 2020", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_calendar, null), Color.parseColor("#3E50B4")));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_timeline);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TimelineAdapter adapter = new TimelineAdapter(entries);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter_entries:
                final Dialog dialog = new Dialog(TimelineActivity.this);
                dialog.setContentView(R.layout.dialog_filter);
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}