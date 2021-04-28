package com.example.datale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PinActivity extends AppCompatActivity {

    private static final String TAG = "PinActivity";
    EditText enter_mpin;
    ImageView i1, i2, i3, i4;
    Button[] numbers = new Button[10];
    String pin;

    String pinInput = "";

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

        pin = "1234"; //after, database blabla....

        i1 = (ImageView) findViewById(R.id.imageview_circle1);
        i2 = (ImageView) findViewById(R.id.imageview_circle2);
        i3 = (ImageView) findViewById(R.id.imageview_circle3);
        i4 = (ImageView) findViewById(R.id.imageview_circle4);

        enter_mpin = (EditText) findViewById(R.id.editText_enter_mpin);
        enter_mpin.requestFocus();
        enter_mpin.setInputType(InputType.TYPE_CLASS_NUMBER);
        enter_mpin.setFocusableInTouchMode(true);

        for (int i = 0; i <numbers.length; i++) {
            final int count = i;
            numbers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pinInput += Integer.toString(count);
                    enter_mpin.setText(pinInput);
                }
            });
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
                        i4.setImageResource(R.drawable.circle2);
                        String input = s.toString();
                        Login(input);
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
    public void Login(String p)
    {
        if(p.equals(pin))
        {
            Intent i;
            i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else{
            Toast toast = Toast.makeText(this.getApplicationContext(),"Wrong Pin.", Toast.LENGTH_SHORT);
          
            toast.show();
            i1.setImageResource(R.drawable.circle);
            i2.setImageResource(R.drawable.circle);
            i3.setImageResource(R.drawable.circle);
            i4.setImageResource(R.drawable.circle);
            pinInput = "";
            enter_mpin.setText(pinInput);


        }
    }
}
