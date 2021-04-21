package com.example.datale;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class FragmentTimeline extends Fragment {

    public TimelineAdapter timelineAdapter;

    public FragmentTimeline() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_timeline);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        timelineAdapter = new TimelineAdapter(MainActivity.listOfEntries);
        recyclerView.setAdapter(timelineAdapter);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelineAdapter.notifyDataSetChanged();
                Log.d("#Entry", Integer.toString(MainActivity.listOfEntries.size()));

            }
        });

        return view;
    }
}