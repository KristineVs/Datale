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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ArrayList<Entries> entries = new ArrayList<>();
        entries.add(new Entries("Crazy day", "Dublin", "Feb 20, 2020", "11:00"));
        entries.add(new Entries("Wandering in paris", "Dublin", "Feb 28, 2020", "11:00"));
        entries.add(new Entries("Girls trip", "Dublin", "Mar 20, 2020", "11:00"));
        entries.add(new Entries("Want to go back", "Dublin", "Apr 4, 2020", "11:00"));
        entries.add(new Entries("Kayaking", "Dublin", "Apr 20, 2020", "11:00"));
        entries.add(new Entries("What am I doing", "Dublin", "May 16, 2020", "11:00"));

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
        switch (item.getItemId()) {
            case R.id.filter_entries:
                showFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialog() {
        final Dialog dialog = new Dialog(TimelineActivity.this);
        dialog.setContentView(R.layout.dialog_filter);
        dialog.show();

        final Button buttonCancel = dialog.findViewById(R.id.button_dialog_filter_cancel);
        final Button buttonOk = dialog.findViewById(R.id.button_dialog_filter_ok);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // fill spinners
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("item1");
        spinnerArray.add("item2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = dialog.findViewById(R.id.spinner_sort_by);
        sItems.setAdapter(adapter);
    }
}