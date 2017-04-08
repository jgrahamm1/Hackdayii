package hd.hackdayii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooserDialog;

/**
 * Created by jgraham on 4/8/17.
 * Displays encrypted files
 */

public class SharedActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<File> file_paths = new ArrayList<>();
    ArrayList<String> file_names = new ArrayList<>();
    String m_phoneno;

    public Button get_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);

        // Register listener for buttons
        get_btn = (Button) findViewById(R.id.get_button);
        get_btn.setOnClickListener(this);

        SharedPreferences keyValues = getSharedPreferences("seckey_preferences",
                Context.MODE_PRIVATE);
        String phoneno = keyValues.getString("phoneno", null);
        m_phoneno = phoneno;

        getEncryptedFiles();

        LstAdapter lst_adapter = new LstAdapter(getApplicationContext(), file_paths);
        ListView list_view = (ListView) findViewById(R.id.shared_list);
        Log.d("GETFILES", list_view.getId() + " is ID");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // Login Button
            case R.id.get_button:
                GetFileTask gft = new GetFileTask();
                gft.execute();
                break;

            default:
                Log.d("ERROR", "SharedActivity OnClickListener does not recognize this View");
        }
    }
    class GetFileTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String serv_res;

        /*
         * Let's get this done in the background.
         */
        protected String doInBackground(Void... arg0) {
            try {
                // Put parameters in map
                Map<String, String> params = new HashMap<String, String>();
                params.put("phoneno", m_phoneno);
                // Send to server
                try {
                    serv_res = ServerUtil.get("http://kitabu.prashant.at/api/fetchfiles",
                            params, getBaseContext());
                } catch (IOException e) {
                    Log.d("GETFILES", "Sending to server did not work");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                this.exception = e;
                Log.d("GETFILES", "Async doInBackground failed");
                return null;
            }
            return serv_res;
        }

        /*
         * The background thing ended... so what now?
         */
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d("GETFILE", "onPostExecute got result: " + result);
                String tr = "false";
            }
        } // PostExecute close
    } // Login Asynctask close
}
