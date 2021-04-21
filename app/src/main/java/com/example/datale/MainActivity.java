package com.example.datale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;

    String previousFragmentTag;
    int homeFragmentTag = 0;
    String[] fragmentTags = {"timeline", "calendar", "map", "user"};

    public static ArrayList<Entries> listOfEntries = new ArrayList<>();

    DatabaseReference entryDbRef;
    DatabaseReference personalDbRef;
    Entries entries;
    User user;

    FragmentTimeline fragmentTimeline = new FragmentTimeline();
    FragmentCalendar fragmentCalendar = new FragmentCalendar();
    FragmentMap fragmenMap = new FragmentMap();
    FragmentUser fragmentUser = new FragmentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        FloatingActionButton fabAddEntry = findViewById(R.id.fab_add_entry);

        navView.setBackground(null);
        navView.getMenu().getItem(2).setEnabled(false);

        switchFragmentByTag(homeFragmentTag, fragmentTags[homeFragmentTag]);

        // retrieve entries
        entries = new Entries();
        user = new User();

        entryDbRef = FirebaseDatabase.getInstance().getReference().child("Entries");
        personalDbRef = FirebaseDatabase.getInstance().getReference().child("Personal");

        entryDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.listOfEntries.clear();
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren())
                        MainActivity.listOfEntries.add(child.getValue(Entries.class));
                } catch (DatabaseException e) {

                }
                fragmentTimeline.timelineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fabAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                startActivity(intent);
            }
        });

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switchFragmentByMenuItem(navView, item);
                return true;
            }
        });
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
        final Dialog dialog = new Dialog(MainActivity.this);
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
        List<String> sortBySpinnerArray =  new ArrayList<>();
        sortBySpinnerArray.add("None");
        sortBySpinnerArray.add("Alphabet");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortBySpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sortBySpinnerItems = dialog.findViewById(R.id.spinner_sort_by);
        sortBySpinnerItems.setAdapter(adapter);

        sortBySpinnerItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    // sorting

//                    Collections.sort(entries, new CustomStringComparator());
//                    timelineAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // sorting alphabetically
    private static class CustomStringComparator implements Comparator<Entries> {
        @Override
        public int compare(Entries o1, Entries o2) {
            return o1.getEentry().compareTo(o2.getEentry());
        }
    }

    private void switchFragmentByMenuItem(BottomNavigationView navView, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Timeline:
                switchFragmentByTag(0, fragmentTags[0]);
                navView.getMenu().getItem(0).setChecked(true);
                break;
            case R.id.Calender:
                switchFragmentByTag(1, fragmentTags[1]);
                navView.getMenu().getItem(1).setChecked(true);
                break;
            case R.id.Map:
                switchFragmentByTag(2, fragmentTags[2]);
                navView.getMenu().getItem(3).setChecked(true);
                break;
            case R.id.User:
                switchFragmentByTag(3, fragmentTags[3]);
                navView.getMenu().getItem(4).setChecked(true);
                break;
        }
    }

    private void switchFragmentByTag(int position, String fragmentTagToShow) {
        switch (position) {
            case 0:
                if (fm.findFragmentByTag(fragmentTags[0]) != null)
                    fm.beginTransaction().show(fm.findFragmentByTag(fragmentTags[0])).commit();
                else
                    fm.beginTransaction().add(R.id.frame_layout_fragments, fragmentTimeline, fragmentTags[0]).commit();
                break;
            case 1:
                if (fm.findFragmentByTag(fragmentTags[1]) != null)
                    fm.beginTransaction().show(fm.findFragmentByTag(fragmentTags[1])).commit();
                else
                    fm.beginTransaction().add(R.id.frame_layout_fragments, fragmentCalendar, fragmentTags[1]).commit();
                break;
            case 2:
                if (fm.findFragmentByTag(fragmentTags[2]) != null)
                    fm.beginTransaction().show(fm.findFragmentByTag(fragmentTags[2])).commit();
                else
                    fm.beginTransaction().add(R.id.frame_layout_fragments, fragmenMap, fragmentTags[2]).commit();
                break;
            case 3:
                if (fm.findFragmentByTag(fragmentTags[3]) != null)
                    fm.beginTransaction().show(fm.findFragmentByTag(fragmentTags[3])).commit();
                else
                    fm.beginTransaction().add(R.id.frame_layout_fragments, fragmentUser, fragmentTags[3]).commit();
                break;
        }

        if (!fragmentTagToShow.equals(previousFragmentTag)) {
            if (fm.findFragmentByTag(previousFragmentTag) != null)
                fm.beginTransaction().hide(fm.findFragmentByTag(previousFragmentTag)).commit();

            previousFragmentTag = fragmentTagToShow;
        }
    }
}