package com.example.datale;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class DiaryActivity extends AppCompatActivity {

    EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_entry);

        //Display Date
        date = findViewById(R.id.editDate);

        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance().format(currentTime);

        Log.d("myLOG", currentTime.toString());
        Log.d("myLOG", formattedDate);

        date.setText(formattedDate);
    }
}
