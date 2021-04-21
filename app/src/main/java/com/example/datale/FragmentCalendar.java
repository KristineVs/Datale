package com.example.datale;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class FragmentCalendar extends Fragment {

    Button enterbutton;
    EditText inputname, inputabout;
    EditText inputentry, inputlocation, inputdate, inputtime, inputemoji, inputvideo, inputphoto, inputaudio;
    DatabaseReference entryDbRef;
    DatabaseReference personalDbRef;
    Entries entries;
    User user;

    public FragmentCalendar() {
    }

    public static FragmentCalendar newInstance(String param1, String param2) {
        return new FragmentCalendar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        enterbutton = view.findViewById(R.id.enterbutton);

        entries = new Entries();
        user = new User();

        entryDbRef = FirebaseDatabase.getInstance().getReference().child("Entries");
        personalDbRef = FirebaseDatabase.getInstance().getReference().child("Personal");

        enterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUser(view);
                insertEntry(view);
            }
        });

        return view;
    }

    private void insertUser(View view) {
        inputname = view.findViewById(R.id.inputname);
        inputabout = view.findViewById(R.id.inputabout);

        user.setEname(inputname.getText().toString());
        user.setEabout(inputabout.getText().toString());

        personalDbRef.push().setValue(user);

        System.out.println("Button Clicked");
    }

    private void insertEntry(View view) {
        inputentry = view.findViewById(R.id.inputentry);
        inputlocation = view.findViewById(R.id.inputlocation);
        inputdate = view.findViewById(R.id.inputdate);
        inputtime = view.findViewById(R.id.inputtime);
        inputemoji = view.findViewById(R.id.inputemoji);
        inputvideo = view.findViewById(R.id.inputvideo);
        inputphoto = view.findViewById(R.id.inputphoto);
        inputaudio = view.findViewById(R.id.inputaudio);

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