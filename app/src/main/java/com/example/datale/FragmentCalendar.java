package com.example.datale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class FragmentCalendar extends Fragment {

    public FragmentCalendar() {
    }

    private MaterialCalendarView materialCalendarView;

    public static FragmentCalendar newInstance(String param1, String param2) {
        return new FragmentCalendar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Access to the database, changing the color of the dates
        //when the diary entry exists.



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_calendar, container, false);
        materialCalendarView = viewFragment.findViewById(R.id.calendarView);
        MainActivity.listOfEntries.forEach(entries -> {

            LocalDate localDate = entries.getEdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            CalendarDay calendar = CalendarDay.from(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
                    materialCalendarView.setDateSelected(calendar, true);
                }
        );
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);


        //if click date in calendar, call diary entry of that date
        //Intent intent = new Intent(this,DiaryActivity.class);
        //intent.putExtra(date);
        //startActivity(intent);

        return viewFragment;
    }
}