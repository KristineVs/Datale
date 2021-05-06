package com.example.datale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DiaryActivity extends AppCompatActivity{

    Dialog dialog;

    TextView date;
    Date currentDate;

    TextView locationText;
    LatLng location;
    Double locationLat = 0.0;
    Double locationLong = 0.0;
    Geocoder geocoder;
    List<Address> addresses;
    Context c = this;
    String address;
    SearchView searchView;
    List<Address> addressList;
    GoogleMap map;

    ImageView imageView;
    String currentImage;

    TextView mood;
    int startingEmoji = 0x1F600;
    int currentMood = startingEmoji;

    FloatingActionButton main, microphone, image, cam;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    Boolean isOpen = false;

    EditText editTextTitle;
    EditText editTextEntry;

    private boolean isRecording = false;
    private String audioFileName = "";

    private ImageView imageViewPlayPause;
    private SeekBar seekBarAudio;
    private MediaPlayer mediaPlayer;

    private int whichEntryIsEditing = -1;
    private boolean editingEntry = false;

    DatabaseReference entryDbRef;
    DatabaseReference personalDbRef;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    public static final int CAMERA_REQUEST_CODE = 102;
    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_entry);

        dialog = new Dialog(DiaryActivity.this);
        dialog.setContentView(R.layout.dialog_location);
        main = findViewById(R.id.main);
        cam = findViewById(R.id.cambtn);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anti);

        mood = findViewById(R.id.editMood);
        locationText = findViewById(R.id.editLocation);
        date = findViewById(R.id.editDate);
        microphone = findViewById(R.id.micbtn);

        imageViewPlayPause = findViewById(R.id.image_view_play_pause);
        seekBarAudio = findViewById(R.id.seek_bar_audio);

        mediaPlayer = new MediaPlayer();

        editTextTitle = findViewById(R.id.title_text);
        editTextEntry = findViewById(R.id.editEntry);

        mood.setText(new String(Character.toChars(currentMood)));

        entryDbRef = FirebaseDatabase.getInstance().getReference().child("Entries");
        personalDbRef = FirebaseDatabase.getInstance().getReference().child("Personal");

        imageView = findViewById(R.id.imageView);
        image = findViewById(R.id.imagebtn);

        whichEntryIsEditing = getIntent().getIntExtra("whichEntry", -1);
        if (whichEntryIsEditing != -1) {
            editingEntry = true;
            Entries currentEntry = MainActivity.listOfEntries.get(whichEntryIsEditing);

            editTextTitle.setText(currentEntry.getEtitle());
            editTextEntry.setText(currentEntry.getEentry());
            setSongToMediaPlayer(currentEntry.getEaudio());
            mood.setText(new String(Character.toChars(currentEntry.getEemoji())));
            geocoder = new Geocoder(c, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(currentEntry.getLatitude(), currentEntry.getLongitude(), 1);
                address = addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            locationText.setText(address);
            //locationText.setText(currentEntry.getLatitude() + ":" + currentEntry.getLongitude());

            date.setText(DateFormat.getDateInstance().format(currentEntry.getEdate()));

            if (currentEntry.getEphoto() != null) {
                currentImage = currentEntry.getEphoto();
                if (!currentImage.equals("")) {

                    // this doesnt work for some reason
                    File imgFile = new File(currentImage);
                    if (imgFile.exists()) {
                        Log.d("#path", "works");

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imageView.setImageBitmap(myBitmap);
                    }
                }
            }

        } else {
            // set date to date text view
            Date currentTime;
//            Log.i("test",this.getIntent().getStringExtra("date",));
                String dateParams = this.getIntent().getStringExtra("date");
            if(dateParams == null || dateParams.isEmpty() ) {
                currentTime = Calendar.getInstance().getTime();
            } else {
                LocalDate dateParam = LocalDate.parse(dateParams
                        , DateTimeFormatter.ofPattern("uuuu-M-d"));
                currentTime = Date.from(dateParam.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }



//            Date currentTime = Calendar.getInstance().getTime();
            String formattedDate = DateFormat.getDateInstance().format(currentTime);
            currentDate = currentTime;
            date.setText(formattedDate);

            // disable play button
            imageViewPlayPause.setEnabled(false);
            imageViewPlayPause.setAlpha(0.5f);

        }

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    closeTools();

                } else {
                    openTools();

                }

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });

        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationDialog();
                //Intent intent = new Intent(DiaryActivity.this, SearchLocation.class);
                //startActivity(intent);
            }
        });

        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMoodDialog();
            }
        });

        // Microphone
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecordingDialog();
                closeTools();
            }
        });

        imageViewPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    playAudioFile(audioFileName);
                    imageViewPlayPause.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, null));
                } else {
                    mediaPlayer.pause();
                    imageViewPlayPause.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null));
                }
            }
        });

        //Gallery
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
                closeTools();
            }

        });

        //Camera
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted
                        String[] permissions = {Manifest.permission.CAMERA};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
                closeTools();
            }
        });

    }

    //Camera
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
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
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(DiaryActivity.this, "Permission accepted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DiaryActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);
        }
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageView.setImageURI(data.getData());

            // getting the path of the image
            String path = "";
            if (getContentResolver() != null) {
                Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cursor.getString(idx);
                    cursor.close();
                }
            }
            currentImage = path;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_entry:
                if (whichEntryIsEditing == -1) { // save new entry
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

                    Entries entries = new Entries();
                    entries.setEtitle(editTextTitle.getText().toString());
                    entries.setEentry(editTextEntry.getText().toString());
                    entries.setLatitude(locationLat);
                    entries.setLongitude(locationLong);
                    entries.setEdate(currentDate);
                    entries.setEemoji(currentMood);
                    entries.setEphoto(currentImage);
                    entries.setEaudio(audioFileName);

                    entryDbRef.child(MainActivity.userId).push().setValue(entries);

                    finish();
                } else { // update current entry

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openRecordingDialog() {
        final Dialog dialog = new Dialog(DiaryActivity.this);
        dialog.setContentView(R.layout.dialog_audio);
        dialog.show();

        final View viewIconRecording = dialog.findViewById(R.id.view_icon_recording);
        final TextView textViewRecordingStatus = dialog.findViewById(R.id.text_view_recording_status);
        final ImageView imageViewMic = dialog.findViewById(R.id.image_view_recording_button);
        final ImageView imageViewButtonOutline = dialog.findViewById(R.id.image_view_recording_button_outline);
        final Button buttonRecordingSave = dialog.findViewById(R.id.button_dialog_recording_ok);
        final Button buttonRecordingPlay = dialog.findViewById(R.id.button_dialog_recording_play);
        final Button buttonRecordingCancel = dialog.findViewById(R.id.button_dialog_recording_cancel);

        setRecordingStatus(textViewRecordingStatus, viewIconRecording, imageViewButtonOutline, buttonRecordingPlay, isRecording);

        final MediaRecorder mediaRecorder = new MediaRecorder();
        final StringBuilder fileName = new StringBuilder();

        imageViewMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted
                        String[] permissions = {Manifest.permission.RECORD_AUDIO};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        if (!isRecording) {
                            fileName.setLength(0);
                            fileName.append(getFilesDir().getAbsolutePath())
                                    .append(File.separator)
                                    .append(new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.ENGLISH).format(new Date())).append(".3gp");

                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mediaRecorder.setOutputFile(fileName.toString());
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            try {
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            mediaRecorder.stop();

                            setSongToMediaPlayer(fileName.toString());
                        }

                        isRecording = !isRecording;
                        setRecordingStatus(textViewRecordingStatus, viewIconRecording, imageViewButtonOutline, buttonRecordingPlay, isRecording);
                    }
                }
            }
        });

        buttonRecordingPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioFile(fileName.toString());
            }
        });

        buttonRecordingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fileName.toString().equals("")) {
                    audioFileName = fileName.toString();
                    setSongToMediaPlayer(audioFileName);
                }
                dialog.dismiss();
                mediaRecorder.release();
            }
        });

        buttonRecordingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mediaRecorder.release();
            }
        });
    }

    private void openMoodDialog() {
        int[] unicodeEmojis = {
                0x1F600,
                0x1F601,
                0x1F606,
                0x1F923,
                0x1F607,
                0x1F970,
                0x1F60D,
                0x1F60B,
                0x1F917,
                0x1F92B,
                0x1F611,
                0x1F62C,
                0x1F614,
                0x1F634,
                0x1F92E,
                0x1F60E,
                0x2639,
                0x1F97A,
                0x1F62D,
                0x1F622,
                0x1F61E,
                0x1F624,
                0x1F621,
                0x1F62B
        };

        final Dialog dialog = new Dialog(DiaryActivity.this);
        dialog.setContentView(R.layout.dialog_mood);
        dialog.show();

        Button buttonCancel = dialog.findViewById(R.id.button_dialog_mood_cancel);
        LinearLayout linearLayout = dialog.findViewById(R.id.linear_layout_emoji);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // each row is represented by linear layout filled with four emojis

        for (int i = 0; i < 5; i++) {
            ConstraintLayout constraintLayout = new ConstraintLayout(DiaryActivity.this);
            constraintLayout.setId(999999);
            ConstraintLayout.LayoutParams constraintLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            if (i != 4)
                constraintLayoutParams.setMargins(0, 0, 0, 32);
            constraintLayout.setLayoutParams(constraintLayoutParams);

            for (int j = 0; j < 4; j++) {

                TextView textViewEmoji = new TextView(DiaryActivity.this);
                TextView textViewEmojiCount = new TextView(DiaryActivity.this);

                constraintLayout.addView(textViewEmoji);
                constraintLayout.addView(textViewEmojiCount);

                ConstraintLayout.LayoutParams textEmojiConstraints = (ConstraintLayout.LayoutParams) textViewEmojiCount.getLayoutParams();
                ConstraintLayout.LayoutParams textEmojisCountConstraints = (ConstraintLayout.LayoutParams) textViewEmoji.getLayoutParams();

                // text emoji
                textViewEmoji.setId(1000 + j * i + j);
                textViewEmoji.setTextColor(Color.BLACK);
                textViewEmoji.setTextSize(30);
                textViewEmoji.setText(new String(Character.toChars(unicodeEmojis[j * i + j])));
                textViewEmoji.setFocusable(true);
                textViewEmoji.setClickable(true);
                textViewEmoji.setPadding(16, 16, 16, 16);

                textEmojiConstraints.startToStart = textViewEmoji.getId();
                textEmojiConstraints.endToEnd = textViewEmoji.getId();
                textEmojiConstraints.topToBottom = textViewEmoji.getId();
                textViewEmojiCount.setLayoutParams(textEmojiConstraints);

                // text emoji count
                textViewEmojiCount.setId(2000 + j * i + j);
                textViewEmojiCount.setText("");

                textEmojisCountConstraints.topToTop = constraintLayout.getId();
                textEmojisCountConstraints.startToStart = constraintLayout.getId();
                textEmojisCountConstraints.endToEnd = constraintLayout.getId();
                textEmojisCountConstraints.horizontalBias = 0.33f * j;
                textViewEmoji.setLayoutParams(textEmojisCountConstraints);

                final int row = i;
                final int column = j;
                textViewEmoji.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentMood = unicodeEmojis[column * row + column];
                        mood.setText(new String(Character.toChars(currentMood)));
                        dialog.dismiss();
                    }
                });
            }
            linearLayout.addView(constraintLayout);
        }
    }

    private void openLocationDialog() {


        dialog.show();

        final Button buttonCancel = dialog.findViewById(R.id.button_dialog_location_cancel);
        final Button buttonOk = dialog.findViewById(R.id.button_dialog_location_ok);
        final EditText editTextSearch = dialog.findViewById(R.id.edit_text_dialog_location);

        searchView = dialog.findViewById(R.id.searchLocation);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.dialogMap);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                String addressName = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (addressName != null || addressName.equals("")) {
                    Geocoder geocoder = new Geocoder(DiaryActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(addressName, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(addressName));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    locationLat = latLng.latitude;
                    locationLong = latLng.longitude;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                map.clear();
                return false;
            }
        });

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions locationSelected = new MarkerOptions();

                        locationSelected.position(latLng);

                        locationSelected.title(latLng.latitude + " : " + latLng.longitude);

                        googleMap.clear();

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                        googleMap.addMarker(locationSelected);

                        locationLat = latLng.latitude;
                        locationLong = latLng.longitude;
                    }
                });

            }

        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(DiaryActivity.this, editTextSearch.getText().toString(), Toast.LENGTH_SHORT).show();
                location = new LatLng(locationLat, locationLong);
                geocoder = new Geocoder(c, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(locationLat, locationLong, 1);
                    address = addresses.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                locationText.setText(address);
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


        /*searchView = dialog.findViewById(R.id.searchLocation);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.dialogMap);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(DiaryActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(1);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);*/



    private void openDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(DiaryActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date currentDateDialog = new GregorianCalendar(year, month - 1, dayOfMonth).getTime();
                currentDate = currentDateDialog;
                date.setText(DateFormat.getDateInstance().format(currentDateDialog));
            }
        }, 2021, 2, 2);

        datePickerDialog.show();
        datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purple_500));
        datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purple_500));
    }

    private void playAudioFile(String fileName) {
        final long start = System.currentTimeMillis() - mediaPlayer.getCurrentPosition();
        mediaPlayer.start();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer.isPlaying()) {
                    long elapsedTimeMillis = System.currentTimeMillis() - start;
                    int currentPositionPercent = ((int) ((float) elapsedTimeMillis / mediaPlayer.getDuration() * 100));
                    seekBarAudio.setProgress(currentPositionPercent);
                }
            }
        });
    }

    private void setSongToMediaPlayer(String fileName) {
        try {
            mediaPlayer = new MediaPlayer();
            File directory = new File(fileName);
            FileInputStream fis = new FileInputStream(directory);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();

            // enable button
            imageViewPlayPause.setEnabled(true);
            imageViewPlayPause.setAlpha(1f);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    imageViewPlayPause.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null));
                    mediaPlayer.seekTo(0);
                }
            });

            seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        if (mediaPlayer.isPlaying()) {
                            imageViewPlayPause.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null));
                            mediaPlayer.pause();
                        }
                        mediaPlayer.seekTo((int) ((float) progress / 100f * mediaPlayer.getDuration()));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignore) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void openTools() {
        image.startAnimation(fab_open);
        cam.startAnimation(fab_open);
        microphone.startAnimation(fab_open);
        main.startAnimation(fab_clock);
        image.setClickable(true);
        cam.setClickable(true);
        microphone.setClickable(true);
        isOpen = true;
    }

    private void closeTools() {
        image.startAnimation(fab_close);
        cam.startAnimation(fab_close);
        microphone.startAnimation(fab_close);
        main.startAnimation(fab_anticlock);
        image.setClickable(false);
        cam.setClickable(false);
        microphone.setClickable(false);
        isOpen = false;
    }

    private void setRecordingStatus(TextView textViewStatus, View viewIconStatus, ImageView imageViewButtonOutline, Button buttonPlay, boolean isRecording) {
        String isRecordingText = "Recording ...";
        String isNotRecordingText = "Not recording";

        if (!isRecording) {
            buttonPlay.setEnabled(true);
            buttonPlay.setAlpha(1f);
            imageViewButtonOutline.setAlpha(0f);
            viewIconStatus.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_not_recording, null));
            textViewStatus.setText(isNotRecordingText);
        } else {
            buttonPlay.setEnabled(false);
            buttonPlay.setAlpha(0.5f);
            imageViewButtonOutline.setAlpha(1f);
            viewIconStatus.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_recording, null));
            textViewStatus.setText(isRecordingText);
        }
    }
}
