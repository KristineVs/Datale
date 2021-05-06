package com.example.datale;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.maps.model.Marker;
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
    int init = 0;
    int length = 0;
    ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
    ArrayList<LatLng> coordinates = new ArrayList<LatLng>();
    GoogleMap map;
    Context context;
    int position = -1;

    public static FragmentMap newInstance(String param1, String param2) {
        return new FragmentMap();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        for( int i = 0; i < MainActivity.listOfEntries.size(); i++) {

            if (MainActivity.listOfEntries.get(i).getLongitude() != 0.0 && MainActivity.listOfEntries.get(i).getLatitude() != 0.0) {
                coordinates.add(new LatLng(MainActivity.listOfEntries.get(i).getLatitude(), MainActivity.listOfEntries.get(i).getLongitude()));
                markers.add(new MarkerOptions());
            }
        }

        length = coordinates.size();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googelMap);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                init++;
                for( int i = 0; i < length; i++){

                    markers.get(i).position(coordinates.get(i));
                    markers.get(i).title(MainActivity.listOfEntries.get(i).getEtitle());
                    googleMap.addMarker(markers.get(i));

                }
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        for(int i = 0; i < length; i++){
                            if(marker.getPosition().equals(markers.get(i).getPosition()) ){
                                position = i;
                            }
                        }

                        Intent diaryEntryIntent = new Intent(getActivity(), DiaryActivity.class);
                        diaryEntryIntent.putExtra("whichEntry", position);
                        getActivity().startActivity(diaryEntryIntent);

                    }
                });

            }

        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (init > 0){
            refresh();
        }
    }

    private void refresh(){
        length = MainActivity.listOfEntries.size();

        markers.clear();
        coordinates.clear();

        if(map != null){
            map.clear();
        }

        for( int i = 0; i < length; i++) {
            markers.add(new MarkerOptions());
            coordinates.add(new LatLng(MainActivity.listOfEntries.get(i).getLatitude(), MainActivity.listOfEntries.get(i).getLongitude()));
            markers.get(i).position(coordinates.get(i));
            markers.get(i).title(MainActivity.listOfEntries.get(i).getEtitle());
            map.addMarker(markers.get(i));
        }
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