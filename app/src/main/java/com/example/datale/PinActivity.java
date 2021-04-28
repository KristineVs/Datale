package com.example.datale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PinActivity extends AppCompatActivity {

    private static final String TAG = "PinActivity";
    EditText enter_mpin;
    TextView textViewPrompt;
    ImageView i1, i2, i3, i4;
    Button[] numbers = new Button[10];

    String pinSHA256;
    String userId;

    String currentPinInput = "";

    SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        numbers[0] = findViewById(R.id.button_zero);
        numbers[1] = findViewById(R.id.button_one);
        numbers[2] = findViewById(R.id.button_two);
        numbers[3] = findViewById(R.id.button_three);
        numbers[4] = findViewById(R.id.button_four);
        numbers[5] = findViewById(R.id.button_five);
        numbers[6] = findViewById(R.id.button_six);
        numbers[7] = findViewById(R.id.button_seven);
        numbers[8] = findViewById(R.id.button_eight);
        numbers[9] = findViewById(R.id.button_nine);

        i1 = findViewById(R.id.imageview_circle1);
        i2 = findViewById(R.id.imageview_circle2);
        i3 = findViewById(R.id.imageview_circle3);
        i4 = findViewById(R.id.imageview_circle4);

        textViewPrompt = findViewById(R.id.text_view_pin_prompt);
        enter_mpin = (EditText) findViewById(R.id.editText_enter_mpin);
        enter_mpin.requestFocus();
        enter_mpin.setInputType(InputType.TYPE_CLASS_NUMBER);
        enter_mpin.setFocusableInTouchMode(true);

        for (int i = 0; i < numbers.length; i++) {
            final int count = i;
            numbers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPinInput += Integer.toString(count);
                    enter_mpin.setText(currentPinInput);
                }
            });
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pinSHA256 = preferences.getString("saved_pin", "");

        // first time opening app
        if (pinSHA256.equals("")) {
            textViewPrompt.setText("Set PIN");

            // generate userId from date
            SimpleDateFormat currentDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            userId = sha256(currentDatetime.format(new Date()));
        }

        enter_mpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "onKey: screen key pressed");
                switch (s.length()) {
                    case 4:
                        try {
                            i4.setImageResource(R.drawable.circle2);
                            if (pinSHA256.equals("")) {
                                // encrypt pin and save it with userId
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("saved_pin", sha256(s.toString()));
                                editor.apply();

                                Login("");
                            } else
                                Login(sha256(s.toString()));
                        } catch (Exception e) {

                        }
                        break;
                    case 3:
                        i4.setImageResource(R.drawable.circle);
                        i3.setImageResource(R.drawable.circle2);
                        break;
                    case 2:
                        i3.setImageResource(R.drawable.circle);
                        i2.setImageResource(R.drawable.circle2);
                        break;
                    case 1:
                        i2.setImageResource(R.drawable.circle);
                        i1.setImageResource(R.drawable.circle2);
                        break;
                    default:
                        i1.setImageResource(R.drawable.circle);
                }

            }
        });
    }

    public String sha256(final String base) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void Login(String p) {
        if (p.equals(pinSHA256) || p.equals("")) {
            Intent i;
            i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            Toast toast = Toast.makeText(this.getApplicationContext(), "Wrong Pin.", Toast.LENGTH_SHORT);

            toast.show();
            i1.setImageResource(R.drawable.circle);
            i2.setImageResource(R.drawable.circle);
            i3.setImageResource(R.drawable.circle);
            i4.setImageResource(R.drawable.circle);
            currentPinInput = "";
            enter_mpin.setText(currentPinInput);
        }
    }
}
