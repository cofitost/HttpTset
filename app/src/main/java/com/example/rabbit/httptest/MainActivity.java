package com.example.rabbit.httptest;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText accountNumber, userName, password, checkPassword, email, phone;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountNumber = (EditText)findViewById(R.id.ET_signUp_accountNumber);
        userName = (EditText)findViewById(R.id.ET_signUp_name);
        password = (EditText)findViewById(R.id.ET_signUp_PWD);
        checkPassword = (EditText)findViewById(R.id.ET_signUp_checkPWD);
        email = (EditText)findViewById(R.id.ET_signUp_email);
        phone = (EditText)findViewById(R.id.ET_signUp_phone);
        done = (Button)findViewById(R.id.BT_signUp_done);
        done.setOnClickListener(click_done);
    }

    public View.OnClickListener click_done = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onPost();
        }
    };

    public void onPost(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpPost();
            }
        }).start();
    }

    public void httpPost(){
        URL url = null;
        try {
            url = new URL("http://140.134.26.71:42048/android-backend/webapi/user/register");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder params = new Uri.Builder()
                    .appendQueryParameter("userAccount", accountNumber.getText().toString())
                    .appendQueryParameter("userName", userName.getText().toString())
                    .appendQueryParameter("password", password.getText().toString())
                    .appendQueryParameter("email", email.getText().toString())
                    .appendQueryParameter("phone", phone.getText().toString());
            Log.d("abc", params.toString());
            String query = params.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
