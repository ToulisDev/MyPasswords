package com.familyprotection.djasdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;

public class LoginActivity extends AppCompatActivity {

    private static String ipAddress = "djasserver.ddns.net:1433";
    private static String database = "pass";

    EditText emaillogin,passwordlogin;
    Button loginbtn;

    Connection con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        emaillogin = (EditText)findViewById(R.id.et_email);
        passwordlogin = (EditText)findViewById(R.id.et_pass);
        loginbtn = (Button)findViewById(R.id.btn_login);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkConnected()) {
                    new LoginActivity.checkLogin().execute("");
                }else{
                    Toast.makeText(LoginActivity.this, "Check Internet Connection",Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    //Helpful methods
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }



    public class checkLogin extends AsyncTask<String, String, String>{

        String z = null;


        @Override
        protected String doInBackground(String... strings) {
            if(!emaillogin.getText().toString().equals("") && !passwordlogin.getText().toString().equals("")) {
                con = connectionClass(emaillogin.getText().toString(), passwordlogin.getText().toString(), database, ipAddress);
                if (con == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Wrong Input! Check your credentials",Toast.LENGTH_LONG).show();
                        }
                    });
                    z = "On Internet Connection";
                }
                else{
                    ConnectionHelper.username = emaillogin.getText().toString();
                    ConnectionHelper.password = passwordlogin.getText().toString();
                    Intent intent = new Intent(LoginActivity.this, listView.class);
                    startActivity(intent);
                    finish();

                }
            }else
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Check your credentials",Toast.LENGTH_LONG).show();
                    }
                });


            return z;
        }
    }


    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" +server+"/"+database+";user="+user+";password="+password+";";
            connection = DriverManager.getConnection(connectionURL);
        }catch (Exception e){
            Log.e("SQL Connection Error : ",e.getMessage());
        }
        return connection;
    }
}