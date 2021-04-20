package com.example.datale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DiaryActivity extends AppCompatActivity {

    TextView date;
    TextView location;
    ImageView mood, imageView;
    FloatingActionButton microphone, image;

    private boolean isRecording = false;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_entry);

        mood = findViewById(R.id.editMood);
        location = findViewById(R.id.editLocation);
        date = findViewById(R.id.editDate);
        microphone = findViewById(R.id.micbtn);

        // set date to date text view
        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance().format(currentTime);
        date.setText(formattedDate);

        Log.d("myLOG", currentTime.toString());
        Log.d("myLOG", formattedDate);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationDialog();
            }
        });

        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMoodDialog();
            }
        });

        //Gallery
        imageView = findViewById(R.id.imageView);
        image = findViewById(R.id.imagebtn);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
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


//        microphone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.micbtn:
//                        openRecordingDialog();
//                        break;
//                    case R.id.imagebtn:
//
//                        break;
//                    case R.id.videobtn:
//
//                        break;
//                }
//            }
//        });
    }

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
                    pickImageFromGallery();
                }
                else {
                    Toast.makeText(DiaryActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageView.setImageURI(data.getData());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_entry:
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
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
                if (!isRecording) {

                    fileName.setLength(0);
                    fileName.append(getExternalFilesDir(null).getAbsolutePath())
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
                }

                isRecording = !isRecording;
                setRecordingStatus(textViewRecordingStatus, viewIconRecording, imageViewButtonOutline, buttonRecordingPlay, isRecording);
            }
        });

        buttonRecordingPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                FileInputStream fis = null;
                try {
                    File directory = new File(fileName.toString());
                    fis = new FileInputStream(directory);
                    mediaPlayer.setDataSource(fis.getFD());
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException ignore) {
                        }
                    }

                }
            }
        });

        buttonRecordingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                textViewEmojiCount.setText("0");

                textEmojisCountConstraints.topToTop = constraintLayout.getId();
                textEmojisCountConstraints.startToStart = constraintLayout.getId();
                textEmojisCountConstraints.endToEnd = constraintLayout.getId();
                textEmojisCountConstraints.horizontalBias = 0.33f * j;
                textViewEmoji.setLayoutParams(textEmojisCountConstraints);
            }
            linearLayout.addView(constraintLayout);
        }
    }

    private void openLocationDialog() {
        final Dialog dialog = new Dialog(DiaryActivity.this);
        dialog.setContentView(R.layout.dialog_location);

        dialog.show();

        final Button buttonCancel = dialog.findViewById(R.id.button_dialog_location_cancel);
        final Button buttonOk = dialog.findViewById(R.id.button_dialog_location_ok);
        final EditText editTextSearch = dialog.findViewById(R.id.edit_text_dialog_location);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiaryActivity.this, editTextSearch.getText().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void openDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(DiaryActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date currentDate = new GregorianCalendar(year, month - 1, dayOfMonth).getTime();
                date.setText(DateFormat.getDateInstance().format(currentDate));
            }
        }, 2021, 2, 2);

        datePickerDialog.show();
        datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purple_500));
        datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purple_500));
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
