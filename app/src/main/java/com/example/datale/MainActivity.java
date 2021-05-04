package com.example.datale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;

    String previousFragmentTag;
    int homeFragmentTag = 0;
    String[] fragmentTags = {"timeline", "calendar", "map", "user"};

    public static ArrayList<Entries> listOfEntries = new ArrayList<>();
    public static ArrayList<Entries> listOfEntriesBackup = new ArrayList<>();

    String[] sortingKeys = {"None", "Alphabetically", "Date"};
    public static int currentSortKeyPosition = 0;

    String[] entriesWith = {"Anything", "Image", "Video", "Voice"};
    public static int currentEntriesWith = 0;

    DatabaseReference entryDbRef;
    Entries entries;

    FragmentTimeline fragmentTimeline = new FragmentTimeline();
    FragmentCalendar fragmentCalendar = new FragmentCalendar();
    FragmentMap fragmenMap = new FragmentMap();
    FragmentUser fragmentUser = new FragmentUser();

    public static String userId;

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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = preferences.getString("user_id", "");

        if (userId.equals("")) {
            // generate userId from date
            SimpleDateFormat currentDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            userId = PinActivity.sha256(currentDatetime.format(new Date()));

            // save userId
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_id", userId);
            editor.apply();
        }

        // retrieve entries
        entries = new Entries();

        entryDbRef = FirebaseDatabase.getInstance().getReference().child("Entries").child(userId);

        showSearch();

        entryDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.listOfEntries.clear();
                MainActivity.listOfEntriesBackup.clear();
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        listOfEntries.add(child.getValue(Entries.class));
                        listOfEntriesBackup.add(child.getValue(Entries.class));
                    }
                } catch (DatabaseException e) {

                }
                Log.d("#Sort", Integer.toString(0));
                sortEntries(currentSortKeyPosition);
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
    protected void onResume() {
        super.onResume();

        Log.d("#Sort", Integer.toString(1));

        sortEntries(currentSortKeyPosition);
        filterEntries(currentEntriesWith);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("#search", item.getTitle().toString());
        switch (item.getItemId()) {
            case R.id.filter_entries:
                Log.d("#search", "here filter");
                showFilterDialog();
                return true;
            case R.id.app_bar_search:
                Log.d("#search", "here");
                showSearch();
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
        Spinner spinnerSortBy = dialog.findViewById(R.id.spinner_sort_by);
        Spinner spinnerEntriesWith = dialog.findViewById(R.id.spinner_entries_with);

        List<String> sortBySpinnerArray = new ArrayList<>(Arrays.asList(sortingKeys));
        List<String> entriesWithSpinnerArray = new ArrayList<>(Arrays.asList(entriesWith));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortBySpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(adapter);
        spinnerSortBy.setSelection(currentSortKeyPosition);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, entriesWithSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEntriesWith.setAdapter(adapter);
        spinnerEntriesWith.setSelection(currentEntriesWith);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("#Dialog", "Cancel");
                dialog.dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("#Dialog", "OK");
                filterEntries(currentEntriesWith);
                Log.d("#Sort", Integer.toString(2));

                sortEntries(currentSortKeyPosition);

                dialog.dismiss();
            }
        });

        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSortKeyPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerEntriesWith.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentEntriesWith = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sortEntries(int position) {

        if (fragmentTimeline.timelineAdapter != null) {
            if (sortingKeys[position].equals(sortingKeys[1]))
                Collections.sort(MainActivity.listOfEntries, new CustomStringComparator());
            else if (sortingKeys[position].equals(sortingKeys[2]))
                Collections.sort(MainActivity.listOfEntries, new CustomDateComparator());

            // TODO sort on resume after adding
            // TODO ikone poveci
            // TODO save k editas entry
            // TODO mic dialog

            fragmentTimeline.timelineAdapter.notifyDataSetChanged();
        }
    }

    private void filterEntries(int position) {
        if (fragmentTimeline.timelineAdapter != null) {

            listOfEntries.clear();

            if (entriesWith[position].equals(entriesWith[0]))
                listOfEntries.addAll(listOfEntriesBackup);

            else if (entriesWith[position].equals(entriesWith[1]))
                for (Entries entry : listOfEntriesBackup)
                    if (entry.getEphoto() != null)
                        listOfEntries.add(entry);

                    else if (entriesWith[position].equals(entriesWith[2]))
                        for (Entries entry1 : listOfEntriesBackup)
                            if (entry1.getEvideo() != null)
                                listOfEntries.add(entry1);

                            else if (entriesWith[position].equals(entriesWith[3]))
                                for (Entries entry2 : listOfEntriesBackup)
                                    if (entry2.getEaudio() != null)
                                        listOfEntries.add(entry2);

            fragmentTimeline.timelineAdapter.notifyDataSetChanged();
        }
    }

    private void showSearch(){
        Log.d("#search", "here");
        final Dialog search_dialog = new Dialog(MainActivity.this);
        search_dialog.setContentView(R.layout.search);
        search_dialog.show();

        listOfEntries.clear();
        Log.d("#search", "here");
        SearchView search_bar = (SearchView) findViewById(R.id.search_bar);
        //String search_word = search_bar.getQuery().toString();

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("#search", "here");

                if (fragmentTimeline.timelineAdapter != null) {
                    for (Entries entry : listOfEntriesBackup) {
                        if (entry.etitle.contains(query)) {
                            listOfEntries.add(entry);
                            Log.d("#search", "here");
                        }
                    }
                    fragmentTimeline.timelineAdapter.notifyDataSetChanged();
                    Log.d("#search", "changes?");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("search!!!!", "here");
                return false;
            }
            /*
            @Override
            public boolean onQueryTextChange(String newText) {
                if (search_bar.getQuery().length() == 0) {
                    renderList(true);
                }
                return false;
            }*/
        });

    }

    private static class CustomStringComparator implements Comparator<Entries> {
        @Override
        public int compare(Entries o1, Entries o2) {
            return o1.getEtitle().toLowerCase().compareTo(o2.getEtitle().toLowerCase());
        }
    }

    private static class CustomDateComparator implements Comparator<Entries> {
        @Override
        public int compare(Entries o1, Entries o2) {
            return o1.getEdate().compareTo(o2.getEdate());
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