package hd.hackdayii;

import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    KeyPairGenerator kpg;
    KeyPair kp;
    protected Button login_button; // Login button
    protected EditText phone_etxt; // Phone Number EditText
    PublicKey publicKey;
    PrivateKey privateKey;
    private static final int READ_REQUEST_CODE = 42;

    void fetchdata() {
        String serv_res = "";
        Map<String, String> params = new HashMap<String, String>();
        try {
            serv_res = ServerUtil.get("http://kitabu.prashant.at/api/register", params, getApplicationContext());
            Log.d("Register response", serv_res);
            if(serv_res.equals("false"))
            {

            }
            else
            {

            }
            serv_res = ServerUtil.get("http://kitabu.prashant.at/api/getkeys", params, getApplicationContext());
            Log.d("Getkeys response", serv_res);
            if(serv_res.equals("false"))
            {

            }
            else
            {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void generate_keypair()
    {
        Log.d("Generating", "Keypair");
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            kp = kpg.genKeyPair();


            privateKey = kp.getPrivate();
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null);
            KeyStore.PrivateKeyEntry pkEntry =
                    new KeyStore.PrivateKeyEntry(privateKey,null);

            ks.setEntry("privateKey", pkEntry, null);

            // store away the keystore
            try (FileOutputStream fos = new FileOutputStream("seckeystore")) {
                ks.store(fos, null);
            }

            publicKey = kp.getPublic();

            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),
                    RSAPublicKeySpec.class);

            CryptoPKI cr = new CryptoPKI();

            cr.saveToFile(cr.MY_PUBLIC_KEY_FILE,
                    pub.getModulus(), pub.getPublicExponent());

            SharedPreferences sharedPreferences = getSharedPreferences("seckey_preferences",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("flag", true);
            editor.commit();

        } catch (NoSuchAlgorithmException ns) {
            ns.printStackTrace();
        } catch (KeyStoreException kse) {
            kse.printStackTrace();
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (java.security.cert.CertificateException ce) {
            ce.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class FetchDataAsyncTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... params) {
            fetchdata();
            return null;
        }
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);


            SharedPreferences sharedPreferences = getSharedPreferences("seckey_preferences",
                    Context.MODE_PRIVATE);
           // SharedPreferences.Editor editor = sharedPreferences.edit();

            boolean flag;

            flag = sharedPreferences.getBoolean("flag", false);

            Log.d("flag", String.valueOf(flag));

            if(flag == false)
            {
                generate_keypair();
            }

            FetchDataAsyncTask asyncTask = new FetchDataAsyncTask();
        asyncTask.execute();
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
