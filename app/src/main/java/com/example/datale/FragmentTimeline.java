package com.example.datale;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentTimeline extends Fragment {

    ArrayList<Entries> entries = new ArrayList<>();
    TimelineAdapter timelineAdapter;

    public FragmentTimeline() {
    }

    public static FragmentTimeline newInstance(String param1, String param2) {
        return new FragmentTimeline();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        entries.add(new Entries("Crazy day", "Dublin", "Feb 20, 2020", "11:00"));
        entries.add(new Entries("Wandering in paris", "Dublin", "Feb 28, 2020", "11:00"));
        entries.add(new Entries("Girls trip", "Dublin", "Mar 20, 2020", "11:00"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_timeline);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        timelineAdapter = new TimelineAdapter(entries);
        recyclerView.setAdapter(timelineAdapter);

        return view;
    }
}