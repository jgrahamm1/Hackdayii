package hd.hackdayii;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by jgraham on 4/8/17.
 */

public class ContactsActivity extends ListActivity implements View.OnClickListener {

    protected Button share_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile is dead",
                "Blackberry seriously sucks", "WebOS", "Ubuntu Sucks", "Windows7 Sucks", "Max OS X Is Best",
                "Linux is ok", "OS/2" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        // Register listener for buttons
        share_button = (Button) findViewById(R.id.share_button);
        share_button.setOnClickListener(this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }

    /*
    * When buttons are clicked
    */
    @Override
    public void onClick(View v) {
        // Determine if user pressed 'Login' or 'Register'
        switch (v.getId()) {

            // Login Button
            case R.id.share_button:
                Intent login_intent = new Intent(v.getContext(), ContactsActivity.class);
                startActivity(login_intent);
                break;

            default:
                Log.d("ERROR", "ContactsActivity OnClickListener does not recognize this View");
        }
    }

}
