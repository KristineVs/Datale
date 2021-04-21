package com.example.datale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    ArrayList<Entries> entries = new ArrayList<>();
    TimelineAdapter timelineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        entries.add(new Entries("Crazy day", "Dublin", "Feb 20, 2020", "11:00"));
        entries.add(new Entries("Wandering in paris", "Dublin", "Feb 28, 2020", "11:00"));
        entries.add(new Entries("Girls trip", "Dublin", "Mar 20, 2020", "11:00"));


        RecyclerView recyclerView = findViewById(R.id.recycler_view_timeline);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        timelineAdapter = new TimelineAdapter(entries);
        recyclerView.setAdapter(timelineAdapter);
    }
}