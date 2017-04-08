package hd.hackdayii;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    public static int PERMISSION_REQUEST_CODE = 4;

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

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            // Your app already has the permission to access files and folders
            // so you can simply open FileChooser here.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            }
        }
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
                if (validate()) {
                    Intent login_intent = new Intent(v.getContext(), FileActivity.class);
                    startActivity(login_intent);
                }
                break;

            default:
                Log.d("ERROR", "MainActivity OnClickListener does not recognize this View");
        }
    }

    // Validate the EditText fields
    public boolean validate() {
        boolean valid = true;
        String mobile = phone_etxt.getText().toString();


        if (mobile.isEmpty() || mobile.length() != 10) {
            phone_etxt.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            phone_etxt.setError(null);
        }

        Log.d("LOGIN", "Validation result is " + valid);
        return valid;
    }
}
