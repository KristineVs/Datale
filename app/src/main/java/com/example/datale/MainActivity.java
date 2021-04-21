package com.example.datale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;
    FragmentTransaction transaction;

    String previousFragmentTag;
    int homeFragmentTag = 0;
    String[] fragmentTags = {"timeline", "calendar", "map", "user"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        FloatingActionButton fabAddEntry = findViewById(R.id.fab_add_entry);

        switchFragmentByTag(homeFragmentTag, fragmentTags[homeFragmentTag]);

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
                    fm.beginTransaction().add(R.id.frame_layout_fragments, new FragmentTimeline(), fragmentTags[0]).commit();
                break;
            case 1:
                if (fm.findFragmentByTag(fragmentTags[1]) != null)
                    fm.beginTransaction().show(fm.findFragmentByTag(fragmentTags[1])).commit();
                else
                    fm.beginTransaction().add(R.id.frame_layout_fragments, new FragmentCalendar(), fragmentTags[1]).commit();
                break;
            case 2:
                if (fm.findFragmentByTag(fragmentTags[2]) != null)
                    fm.beginTransaction().show(fm.findFragmentByTag(fragmentTags[2])).commit();
                else
                    fm.beginTransaction().add(R.id.frame_layout_fragments, new FragmentMap(), fragmentTags[2]).commit();
                break;
            case 3:
                if (fm.findFragmentByTag(fragmentTags[3]) != null)
                    fm.beginTransaction().show(fm.findFragmentByTag(fragmentTags[3])).commit();
                 else
                    fm.beginTransaction().add(R.id.frame_layout_fragments, new FragmentUser(), fragmentTags[3]).commit();
                break;
        }

        if (fm.findFragmentByTag(previousFragmentTag) != null)
            fm.beginTransaction().hide(fm.findFragmentByTag(previousFragmentTag)).commit();

        previousFragmentTag = fragmentTagToShow;
    }
}