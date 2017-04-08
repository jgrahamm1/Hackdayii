package hd.hackdayii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*
** MainActivity: The launch screen of the application
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button login_button; // Login button
    protected EditText phone_etxt; // Phone Number EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register listener for buttons
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);

        phone_etxt = (EditText) findViewById(R.id.input_phone);
    }

    /*
    * When buttons are clicked
    */
    @Override
    public void onClick(View v) {
        // Determine if user pressed 'Login' or 'Register'
        switch (v.getId()) {

            // Login Button
            case R.id.login_button:
                Intent login_intent = new Intent(v.getContext(), FileActivity.class);
                startActivity(login_intent);
                break;

            default:
                Log.d("ERROR", "MainActivity OnClickListener does not recognize this View");
        }
    }
}
