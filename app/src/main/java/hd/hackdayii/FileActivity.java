package hd.hackdayii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by jgraham on 4/8/17.
 * FileActivity: This activity shows the user their files and has the buttons that let them share
 * stuff.
 */

public class FileActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button share_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register listener for buttons
        share_button = (Button) findViewById(R.id.share_button);
        share_button.setOnClickListener(this);
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
                Log.d("ERROR", "FileActivity OnClickListener does not recognize this View");
        }
    }

}
