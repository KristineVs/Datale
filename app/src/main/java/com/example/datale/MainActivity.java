package com.example.datale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button enterbutton;
    EditText inputname, inputabout;
    EditText inputentry, inputlocation, inputdate, inputtime, inputemoji, inputvideo, inputphoto, inputaudio;
    DatabaseReference entryDbRef;
    DatabaseReference personalDbRef;
    Entries entries;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);

        navView.setBackground(null);
        navView.getMenu().getItem(2).setEnabled(false);

        enterbutton = findViewById(R.id.enterbutton);

        entries = new Entries();
        user = new User();

        entryDbRef = FirebaseDatabase.getInstance().getReference().child("Entries");
        personalDbRef = FirebaseDatabase.getInstance().getReference().child("Personal");

        enterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUser();
                insertEntry();
            }
        });

        findViewById(R.id.buttonTimeline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimelineActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.Entrybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void insertUser() {
        inputname = findViewById(R.id.inputname);
        inputabout = findViewById(R.id.inputabout);

        user.setEname(inputname.getText().toString());
        user.setEabout(inputabout.getText().toString());

        personalDbRef.push().setValue(user);

        System.out.println("Button Clicked");
    }

    private void insertEntry() {

        inputentry = findViewById(R.id.inputentry);
        inputlocation = findViewById(R.id.inputlocation);
        inputdate = findViewById(R.id.inputdate);
        inputtime = findViewById(R.id.inputtime);
        inputemoji = findViewById(R.id.inputemoji);
        inputvideo = findViewById(R.id.inputvideo);
        inputphoto = findViewById(R.id.inputphoto);
        inputaudio = findViewById(R.id.inputaudio);

        entries.setEentry(inputentry.getText().toString());
        entries.setElocation(inputlocation.getText().toString());
        entries.setEdate(inputdate.getText().toString());
        entries.setEtime(inputtime.getText().toString());
        entries.setEemoji(inputemoji.getText().toString());
        entries.setEvideo(inputvideo.getText().toString());
        entries.setEphoto(inputphoto.getText().toString());
        entries.setEaudio(inputaudio.getText().toString());

        entryDbRef.push().setValue(entries);

    }
}