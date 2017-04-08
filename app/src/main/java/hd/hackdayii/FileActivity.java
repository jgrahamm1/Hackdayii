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
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ir.sohreco.androidfilechooser.ExternalStorageNotAvailableException;
import ir.sohreco.androidfilechooser.FileChooserDialog;

import static hd.hackdayii.AESCrypto.encrypt;
import static org.apache.commons.io.FileUtils.readFileToByteArray;

/**
 * Created by jgraham on 4/8/17.
 * FileActivity: This activity shows the user their files and has the buttons that let them share
 * stuff.
 */

public class FileActivity extends AppCompatActivity implements FileChooserDialog.ChooserListener, View.OnClickListener {


    KeyPairGenerator kpg;
    KeyPair kp;
    protected EditText phone_etxt; // Phone Number EditText
    PublicKey publicKey;
    PrivateKey privateKey;
    private static final int READ_REQUEST_CODE = 42;

    // Values to send in putfiles
    public String m_phoneno;
    public String m_data;
    public String m_fname;

    // BUTTOOOOOOOONNNNNN
    public Button view_btn;

    void fetchdata() {
        String pubkey = "";
        String serv_res = "";
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences keyValues = getSharedPreferences("seckey_preferences",
                Context.MODE_PRIVATE);
        String phoneno = keyValues.getString("phoneno", null);
        params.put("phoneno", phoneno);
        m_phoneno = phoneno;
        CryptoPKI cryptoPKI = new CryptoPKI();
        try {
            pubkey = cryptoPKI.readPublicKey().toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        params.put("pubkey", pubkey);
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
                Map<String,?> keys = keyValues.getAll();

                for(Map.Entry<String,?> entry : keys.entrySet()){
                    Log.d("map values",entry.getKey() + ": " +
                            entry.getValue().toString());
                }

                Map<String, String> aMap = new HashMap<String, String>();
                JSONObject jObject = new JSONObject(serv_res);
                JSONArray jsonArray = jObject.getJSONArray("keys");
                Log.d("JSON ARRAY", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject row = jsonArray.getJSONObject(i);
                    aMap.put(row.getString("phoneno"), row.getString("pubkey"));
                }
                Log.d("Hashmap", aMap.toString());

                SharedPreferences.Editor keyValuesEditor = keyValues.edit();

                for (String s : aMap.keySet()) {
                    keyValuesEditor.putString(s, aMap.get(s));
                }

                keyValuesEditor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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

//
//            privateKey = kp.getPrivate();
//            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//            ks.load(null);
//            KeyStore.PrivateKeyEntry pkEntry =
//                    new KeyStore.PrivateKeyEntry(privateKey,null);
//
//            ks.setEntry("privateKey", pkEntry, null);
//
//            // store away the keystore
//            try (FileOutputStream fos = new FileOutputStream("seckeystore")) {
//                ks.store(fos, null);
//            }

            CryptoPKI cr = new CryptoPKI();

            KeyFactory fact = KeyFactory.getInstance("RSA");

            RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(),
                    RSAPrivateKeySpec.class);

            cr.saveToFile(cr.MY_PRIVATE_KEY_FILE,
                    priv.getModulus(), priv.getPrivateExponent());

            RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),
                    RSAPublicKeySpec.class);

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

            Intent intent = getIntent();


            if (intent.getStringExtra("phoneno") != null) {
                SharedPreferences sharedPreferences = getSharedPreferences("seckey_preferences",
                        Context.MODE_PRIVATE);
                String phoneno = intent.getStringExtra("phoneno").toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phoneno", phoneno);
                editor.commit();
            }



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

            // Register listener for buttons
            view_btn = (Button) findViewById(R.id.viewf_button);
            view_btn.setOnClickListener(this);

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

            FetchDataAsyncTask asyncTask = new FetchDataAsyncTask();
            asyncTask.execute();
    }

    @Override
    public void onSelect(String path) {
        String[] selectedFilesPaths = path.split(FileChooserDialog.FILE_NAMES_SEPARATOR);
        // Do whatever you want to do with selected files
        for (int i = 0; i < selectedFilesPaths.length; i++) {
            Log.d("FILES", selectedFilesPaths[i]);
        }

        try {
            readFile(selectedFilesPaths[0]);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String filepath) throws NoSuchAlgorithmException {
        List<Integer> list = new ArrayList<Integer>();
        File file = new File(filepath);
        String fname = file.getName();

        // MD5 Hash the File

        String digest = this.getMD5digest(file);

        SharedPreferences sharedPreferences = getSharedPreferences("",
                Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean flag;

        flag = sharedPreferences.getBoolean("flag", false);

        Log.d("flag", String.valueOf(flag));

        if(flag == false)
        {
            generate_keypair();
        }
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

    private String getMD5digest(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);

            byte[] dataBytes = new byte[1024];

            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            };
            byte[] mdbytes = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            Log.d("md5", "Digest(in hex format):: " + sb.toString());

            //convert the byte to hex format method 2
            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<mdbytes.length;i++) {
                String hex=Integer.toHexString(0xff & mdbytes[i]);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }

            Log.d("md5", "Digest(in hex format):: " + hexString.toString());
            return hexString.toString();
        } catch(NoSuchAlgorithmException nae) {
            nae.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
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

                // Send the file to the server
                m_data = hex;
                m_fname = fname;
                SendFileTask sft = new SendFileTask();
                sft.execute();

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

    class SendFileTask extends AsyncTask<Void, Void, String> {

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
                params.put("data", m_data);
                params.put("filename", m_fname);
                // Send to server
                try {
                    serv_res = ServerUtil.get("http://kitabu.prashant.at/api/putfiles",
                            params, getBaseContext());
                } catch (IOException e) {
                    Log.d("SENDFILES", "Sending to server did not work");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                this.exception = e;
                Log.d("SENDFILES", "Async doInBackground failed");
                return null;
            }
            return serv_res;
        }

        /*
         * The background thing ended... so what now?
         */
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d("SENDFILE", "onPostExecute got result: " + result);
                String tr = "false";
            }
        } // PostExecute close
    } // Login Asynctask close

    /*
* When buttons are clicked
*/
    @Override
    public void onClick(View v) {
        // Determine if user pressed 'Login' or 'Register'
        switch (v.getId()) {

            // Login Button
            case R.id.viewf_button:
                    Intent intent = new Intent(v.getContext(), SharedActivity.class);
                    startActivity(intent);
                break;

            default:
                Log.d("ERROR", "MainActivity OnClickListener does not recognize this View");
        }
    }
}
