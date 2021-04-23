package com.example.datale;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentUser extends Fragment {

    Switch img, cam, mic, location;
    ImageView profile;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    public FragmentUser() {
    }

    public static FragmentUser newInstance(String param1, String param2) {
        return new FragmentUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_user, container, false);

        profile = view.findViewById(R.id.profile);
        img = view.findViewById(R.id.imgswitch);
        cam = view.findViewById(R.id.camswitch);
        mic = view.findViewById(R.id.audioswitch);
        location = view.findViewById(R.id.locationswitch);

        //Profile Pic
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //permission not granted
                        String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else {
                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();
                }

            }

        });

        //Gallery
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //permission not granted
                        String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                }
            }
        });

        //Camera
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (getContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                        //permission not granted
                        String [] permissions = {Manifest.permission.CAMERA};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                }
            }

        });

        //Microphone
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted
                        String[] permissions = {Manifest.permission.RECORD_AUDIO};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                }
            }
        });

        //Location
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted
                        String[] permissions = {Manifest.permission.ACCESS_BACKGROUND_LOCATION};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                }
            }
        });

        return view;
    }


    //Gallery
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    //handle result of permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext(), "Permission allowed!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            profile.setImageURI(data.getData());
        }
    }
}