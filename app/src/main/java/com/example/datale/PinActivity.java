
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PinActivity extends AppCompatActivity {

    private static final String TAG = "PinActivity";
    EditText enter_mpin;
    ImageView i1, i2, i3, i4;
    String pin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin2);

        i1 = (ImageView) findViewById(R.id.imageview_circle1);
        i2 = (ImageView) findViewById(R.id.imageview_circle2);
        i3 = (ImageView) findViewById(R.id.imageview_circle3);
        i4 = (ImageView) findViewById(R.id.imageview_circle4);
        pin = "1234"; //after, database blabla....
        enter_mpin = (EditText) findViewById(R.id.editText_enter_mpin);
        enter_mpin.requestFocus();
        enter_mpin.setInputType(InputType.TYPE_CLASS_NUMBER);
        enter_mpin.setFocusableInTouchMode(true);

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
            //Intent i;
            //i = new Intent(this, MainActivity.class);
            //startActivity(i);
        }
        else{
            Toast toast = Toast.makeText(this.getApplicationContext(),"Wrong Pin.", Toast.LENGTH_SHORT);
          
            toast.show();
            i1.setImageResource(R.drawable.circle);
            i2.setImageResource(R.drawable.circle);
            i3.setImageResource(R.drawable.circle);
            i4.setImageResource(R.drawable.circle);
            enter_mpin.setText("");

        }
    }
}
