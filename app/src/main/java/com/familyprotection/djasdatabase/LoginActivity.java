package com.familyprotection.djasdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.familyprotection.djasdatabase.Models.ConnectionHelper;
import com.familyprotection.djasdatabase.Models.UserRequest;
import com.familyprotection.djasdatabase.Models.UserResponse;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    EditText emaillogin,passwordlogin;
    Button loginbtn,signupbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        emaillogin = findViewById(R.id.et_email);
        passwordlogin = findViewById(R.id.et_pass);
        loginbtn = findViewById(R.id.btn_login);
        signupbtn = findViewById(R.id.btn_signup);

        loginbtn.setOnClickListener((view) -> {
            if(isNetworkConnected()) {
                disableEditText();
                new LoginActivity.checkLogin().returnResult();
            }else{
                Toast.makeText(LoginActivity.this, "Check Internet Connection",Toast.LENGTH_LONG).show();
            }
        });

        signupbtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        SharedPreferences sp = getSharedPreferences("creds",MODE_PRIVATE);
        if(sp.contains("username") && sp.contains("password")){
            String username = sp.getString("username",null);
            String password = sp.getString("password",null);

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Login for PasswordApp")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use account password")
                    .build();
            getBiometricPrompt(username,password).authenticate(promptInfo);
        }

    }

    //Helpful methods
    private BiometricPrompt getBiometricPrompt(String username, String password){
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginActivity.this, errString,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                checkLogin cL = new checkLogin();
                cL.loginUser(cL.createRequest(username,password));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginActivity.this, "Authentication Failed!",Toast.LENGTH_SHORT).show();
            }
        };
        return new BiometricPrompt(this,executor,callback);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void disableEditText() {
        emaillogin.setFocusable(false);
        passwordlogin.setFocusable(false);
        signupbtn.setFocusable(false);
        signupbtn.setClickable(false);
        loginbtn.setFocusable(false);
        loginbtn.setClickable(false);
    }
    private void enableEditText() {
        emaillogin.setFocusable(true);
        passwordlogin.setFocusable(true);
        signupbtn.setFocusable(true);
        signupbtn.setClickable(true);
        loginbtn.setFocusable(true);
        loginbtn.setClickable(true);
    }


    public class checkLogin extends Thread {

        void returnResult() {
            checkLogin thread = new checkLogin();
            thread.start();
            Toast.makeText(LoginActivity.this, "Έλεγχος Στοιχείων", Toast.LENGTH_LONG).show();
            int i = 0;
            while (thread.isAlive()) {
                if(i == 0) {
                    Log.i("INFO", "Thread is busy checking login credentials");
                    i++;
                }
            }
        }

        UserRequest createRequest(String username, String password){
            UserRequest userRequest = new UserRequest();
            userRequest.setUsername(username);
            userRequest.setPassword(password);

            return userRequest;
        }

        void loginUser(UserRequest userRequest){
            Call<UserResponse> userResponseCall = ConnectionHelper.getUserService().loginUser(userRequest);
            userResponseCall.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        ConnectionHelper.token = response.body().getValue();
                        Intent intent = new Intent(LoginActivity.this, listView.class);
                        intent.putExtra("Username",emaillogin.getText().toString());
                        intent.putExtra("Password",passwordlogin.getText().toString());
                        startActivity(intent);
                        finish();
                    }else{
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Λάθος Στοιχεία", Toast.LENGTH_LONG).show());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "An Unexpected Error Occurred!", Toast.LENGTH_LONG).show());
                    Log.e("Error: ","Unexpected Error Occurred", t);
                }
            });

        }


        public void run() {
            if (!emaillogin.getText().toString().equals("") && !passwordlogin.getText().toString().equals("")) {
                loginUser(createRequest(emaillogin.getText().toString(),passwordlogin.getText().toString()));
                runOnUiThread(LoginActivity.this::enableEditText);
            } else {
                runOnUiThread(() -> {
                    enableEditText();
                    Toast.makeText(LoginActivity.this, "Your fields are Empty!", Toast.LENGTH_LONG).show();
                });
            }

        }

    }

}