package hd.hackdayii;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooserDialog;

/**
 * Created by jgraham on 4/8/17.
 * Displays encrypted files
 */

public class SharedActivity extends AppCompatActivity {

    ArrayList<File> file_paths = new ArrayList<>();
    ArrayList<String> file_names = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);

        getEncryptedFiles();

        LstAdapter lst_adapter = new LstAdapter(getApplicationContext(), file_paths);
        ListView list_view = (ListView) findViewById(R.id.shared_list);
        Log.d("MR3", list_view.getId() + " is ID");
        list_view.setAdapter(lst_adapter);
    }

    public void getEncryptedFiles() {
        String path = Environment.getExternalStorageDirectory().toString()+"/Download";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++) {
            if (!files[i].getName().contains(".")) {
                Log.d("Files", "FileName:" + files[i].getName());
                file_paths.add(files[i]);
                file_names.add(files[i].getName());
            }
        }
    }
}
