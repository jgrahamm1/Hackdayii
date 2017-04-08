package hd.hackdayii;

import hd.hackdayii.CryptoPKI;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;



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

        SharedPreferences sharedPreferences = getSharedPreferences("seckey_preferences",
                Context.MODE_PRIVATE);
        String phoneno = sharedPreferences.getString("phoneno", null);

        if (phoneno != null)
        {
            Intent login_intent = new Intent(getApplicationContext(), FileActivity.class);
            login_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login_intent);
        }
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
                    login_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    login_intent.putExtra("phoneno", phone_etxt.toString());
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
