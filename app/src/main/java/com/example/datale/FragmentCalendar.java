package com.example.datale;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

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

        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                for (Entries entries : MainActivity.listOfEntries) {
                    LocalDate localDate = entries.getEdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    CalendarDay calendar = CalendarDay.from(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
//                            materialCalendarView.setDateSelected(calendar, true);
                    if (day.equals(calendar)) {
                        return true;
                    }
                }
                return false;
            }



            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new DotSpan(10, getContext().getResources().getColor(R.color.colorPrimary)));
            }
        });


//        MainActivity.listOfEntries.forEach(entries -> {
//
//            LocalDate localDate = entries.getEdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            CalendarDay calendar = CalendarDay.from(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
//                    materialCalendarView.setDateSelected(calendar, true);
//                }
//        );
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
//        materialCalendarView.setOnDateLongClickListener((widget, date) -> {
//           Log.i("test", "" + date.toString());
//
//            Intent diaryEntryIntent = new Intent(this.getContext(), DiaryActivity.class);
//            diaryEntryIntent.putExtra("date", date.getYear() + "-" + date.getMonth() + "-" +  date.getDay());
//            this.getContext().startActivity(diaryEntryIntent);
//        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                for (int i = 0; i < MainActivity.listOfEntries.size(); i++) {
                    Date entryDate = MainActivity.listOfEntries.get(i).getEdate();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(entryDate);
                    if (date.getDay() == cal.get(Calendar.DAY_OF_MONTH) && date.getMonth() == (cal.get(Calendar.MONTH)+1) && date.getYear() == cal.get(Calendar.YEAR)) {
                        Intent diaryEntryIntent = new Intent(getContext(), DiaryActivity.class);
                        diaryEntryIntent.putExtra("whichEntry", i);
                        getContext().startActivity(diaryEntryIntent);
                        break;
                    }
                }
            }
        });


        //if click date in calendar, call diary entry of that date
//        Intent intent = new Intent(this,DiaryActivity.class);
//        intent.putExtra(date);
//        startActivity(intent);

        return viewFragment;
    }
}