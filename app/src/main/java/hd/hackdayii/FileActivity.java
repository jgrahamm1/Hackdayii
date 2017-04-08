package hd.hackdayii;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jgraham on 4/8/17.
 * FileActivity: This activity shows the user their files and has the buttons that let them share
 * stuff.
 */

public class FileActivity extends ListActivity {

    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile is dead",
                "Blackberry seriously sucks", "WebOS", "Ubuntu Sucks", "Windows7 Sucks", "Max OS X Is Best",
                "Linux is ok", "OS/2" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Action Button Pressed!", Toast.LENGTH_SHORT).show();
//                performFileSearch();
//                Intent docIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                //mediaIntent.setType("video/*"); //set mime type as per requirement
//                startActivityForResult(docIntent, 2);
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//
//        // Filter to only show results that can be "opened", such as a
//        // file (as opposed to a list of contacts or timezones)
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        // Filter to show only images, using the image MIME data type.
//        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
//        // To search for all documents available via installed storage providers,
//        // it would be "*/*".
//        intent.setType("image/*");
//
//        startActivityForResult(intent, READ_REQUEST_CODE);
    }

}
