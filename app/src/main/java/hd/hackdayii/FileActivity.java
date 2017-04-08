package hd.hackdayii;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooserDialog;

import static hd.hackdayii.AESCrypto.encrypt;
import static org.apache.commons.io.FileUtils.readFileToByteArray;

/**
 * Created by jgraham on 4/8/17.
 * FileActivity: This activity shows the user their files and has the buttons that let them share
 * stuff.
 */

public class FileActivity extends AppCompatActivity implements FileChooserDialog.ChooserListener {

    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        String[] values = new String[]{"Android", "iPhone", "WindowsMobile is dead",
                "Blackberry seriously sucks", "WebOS", "Ubuntu Sucks", "Windows7 Sucks", "Max OS X Is Best",
                "Linux is ok", "OS/2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        //setListAdapter(adapter);

        final FileChooserDialog.Builder builder = new FileChooserDialog.Builder(FileChooserDialog.ChooserType.FILE_CHOOSER, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    builder.build().show(getSupportFragmentManager(), null);
                } catch (ExternalStorageNotAvailableException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Action Button Pressed!", Toast.LENGTH_SHORT).show();
//                performFileSearch();
//                Intent docIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                //mediaIntent.setType("video/*"); //set mime type as per requirement
//                startActivityForResult(docIntent, 2);
            }
        });
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        String item = (String) getListAdapter().getItem(position);
//        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
//    }


    @Override
    public void onSelect(String path) {
        String[] selectedFilesPaths = path.split(FileChooserDialog.FILE_NAMES_SEPARATOR);
        // Do whatever you want to do with selected files
        for (int i = 0; i < selectedFilesPaths.length; i++) {
            Log.d("FILES", selectedFilesPaths[i]);
        }

        readFile(selectedFilesPaths[0]);
    }

    public void readFile(String filepath) {
        List<Integer> list = new ArrayList<Integer>();
        File file = new File(filepath);
        String fname = file.getName();
        // Encrypt the file we are reading
        encryptFile(file, catFName(fname));

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                Log.d("FILES", text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public void encryptFile(File file, String fname) {

        // Create byte array from file
        try {
            byte[] byte_array = readFileToByteArray(file);

            // encrypt the byte array
            try {
                String hex_str = encrypt(byte_array);
                Log.d("FILES", hex_str);

                // save the encrypted hex to a file
                saveEncrypted(hex_str, fname);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveEncrypted(String hex, String fname) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), fname);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            try {
                stream.write(hex.getBytes());
                stream.close();
                Log.d("FILES", "Encrypted file saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String catFName(String fname) {
        if (fname.contains(".")) {
            StringBuilder sb = new StringBuilder(fname);
            int index = fname.indexOf(".");
            sb.delete(index, sb.length());
            return sb.toString();
        }
        return fname;
    }
}
