package com.example.datale;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentMap extends Fragment {

    DatabaseReference entryDbRef;
    DatabaseReference personalDbRef;
    String userid = "";
    int whichEntryIsEditing = -1;
    public static ArrayList<Entries> listOfEntries = new ArrayList<>();


    public static FragmentMap newInstance(String param1, String param2) {
        return new FragmentMap();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*userid = getArguments().getString("userid");

        entryDbRef = FirebaseDatabase.getInstance().getReference().child("Entries").child(userid);
        personalDbRef = FirebaseDatabase.getInstance().getReference().child("Personal");

        whichEntryIsEditing = preferences.getString("user_id", "");
        if (whichEntryIsEditing != -1) {
            //editingEntry = true;
            Entries currentEntry = MainActivity.listOfEntries.get(whichEntryIsEditing);

            editTextTitle.setText(currentEntry.getEtitle());
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.googelMap);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();

                        markerOptions.position(latLng);

                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        googleMap.clear();

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                        googleMap.addMarker(markerOptions);
                    }
                });
            }

        });
        return view;
    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //map.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);
        //map.setOnMyLocationClickListener((GoogleMap.OnMyLocationClickListener) this);
        //enableMyLocation();

        LatLng sydney = new LatLng(53.350140, -6.266155);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Diary Entry"));

    }

    private void enableMyLocation() {
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            //PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }*/
}