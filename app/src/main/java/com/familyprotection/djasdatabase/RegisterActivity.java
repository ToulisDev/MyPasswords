package com.familyprotection.djasdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.familyprotection.djasdatabase.Models.ConnectionHelper;
import com.familyprotection.djasdatabase.Models.UserRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {


    EditText emailET,passwordET,confirmPassET;
    Button loginbtn,signupbtn;
    TextView responseMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        emailET = findViewById(R.id.et_email);
        passwordET = findViewById(R.id.et_pass);
        confirmPassET = findViewById(R.id.et_confirm_pass);
        signupbtn = findViewById(R.id.btn_signup);
        loginbtn = findViewById(R.id.btn_login);
        responseMsg = findViewById(R.id.registerResponseMsg);

        loginbtn.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        signupbtn.setOnClickListener(view -> {
            if(isNetworkConnected()) {
                disableEditText();
                new RegisterActivity.registerUser().returnResult();
            }else{
                Toast.makeText(RegisterActivity.this, "Check Internet Connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void disableEditText() {
        emailET.setFocusable(false);
        passwordET.setFocusable(false);
        confirmPassET.setFocusable(false);
        signupbtn.setFocusable(false);
        signupbtn.setClickable(false);
        loginbtn.setFocusable(false);
        loginbtn.setClickable(false);
    }
    private void enableEditText() {
        emailET.setFocusable(true);
        passwordET.setFocusable(true);
        confirmPassET.setFocusable(true);
        signupbtn.setFocusable(true);
        signupbtn.setClickable(true);
        loginbtn.setFocusable(true);
        loginbtn.setClickable(true);
    }

    public class registerUser extends Thread{

        void returnResult(){
            registerUser thread = new registerUser();
            thread.start();
            Toast.makeText(RegisterActivity.this, "Έλεγχος Στοιχείων",Toast.LENGTH_LONG).show();
        }

        UserRequest createRequest(){
            UserRequest userRequest = new UserRequest();
            userRequest.setUsername(emailET.getText().toString());
            userRequest.setPassword(passwordET.getText().toString());

            return userRequest;
        }

        void createUser(UserRequest userRequest){
            Call<String> userResponseCall = ConnectionHelper.getUserService().registerUser(userRequest);
            userResponseCall.enqueue(new Callback<String>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if(response.isSuccessful()){
                        runOnUiThread(() -> {
                            responseMsg.setTextColor(Color.GREEN);
                            responseMsg.setText("Success: User Successfully Registered");
                        });
                    }else{
                        runOnUiThread(() -> {
                            responseMsg.setTextColor(Color.RED);
                            responseMsg.setText("Error: User Already Exists!");
                        });
                    }
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    runOnUiThread(() -> {
                        responseMsg.setTextColor(Color.RED);
                        responseMsg.setText("Error: An Unexpected Error Occurred");
                    });
                    Log.e("Error: ","Failed Sending Request!",t);
                }
            });
        }

        public void run() {
            if(!emailET.getText().toString().equals("") && !passwordET.getText().toString().equals("")) {
                if(passwordET.getText().toString().equals(confirmPassET.getText().toString())){
                    createUser(createRequest());
                    runOnUiThread(RegisterActivity.this::enableEditText);
                }else {
                    runOnUiThread(() ->
                    {
                        enableEditText();
                        Toast.makeText(RegisterActivity.this, "Check your Password", Toast.LENGTH_LONG).show();
                    });
                }
            }else {
                runOnUiThread(() ->
                {
                    enableEditText();
                    Toast.makeText(RegisterActivity.this, "Your fields are Empty!", Toast.LENGTH_LONG).show();
                });
            }
        }

    }

}