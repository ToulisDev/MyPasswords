package com.familyprotection.djasdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView errorInput;
    Button loginbtn,signupbtn;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        emaillogin = findViewById(R.id.et_email);
        passwordlogin = findViewById(R.id.et_pass);
        loginbtn = findViewById(R.id.btn_login);
        signupbtn = findViewById(R.id.btn_signup);
        errorInput = findViewById(R.id.textInput_error);
        loadingDialog = new LoadingDialog(LoginActivity.this);
        loginbtn.setOnClickListener((view) -> {
            if(isNetworkConnected()) {
                disableEditText();
                new LoginActivity.checkLogin().returnResult();
            }else{
                Toast.makeText(LoginActivity.this, R.string.check_internet,Toast.LENGTH_LONG).show();
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
                    .setTitle(getString(R.string.biometric_login))
                    .setSubtitle(getString(R.string.biometric_loginMsg))
                    .setNegativeButtonText(getString(R.string.biometric_loginCnlBox))
                    .build();
            getBiometricPrompt(username,password).authenticate(promptInfo);
        }

    }

    //Helpful methods
    private void resetFields(){
        Drawable defaultField = AppCompatResources.getDrawable(LoginActivity.this, R.drawable.rectangle_1_shape);
        runOnUiThread(() -> {
            errorInput.setVisibility(View.INVISIBLE);
            emaillogin.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            emaillogin.setBackground(defaultField);
            passwordlogin.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            passwordlogin.setBackground(defaultField);
        });
    }
    private void wrongInput(String errorMsg){
        resetFields();
        Drawable errorImg = AppCompatResources.getDrawable(LoginActivity.this, R.drawable.error_field);
        Drawable errorField = AppCompatResources.getDrawable(LoginActivity.this, R.drawable.rectangle_6_shape);
        runOnUiThread(() -> {
            errorInput.setVisibility(View.VISIBLE);
            errorInput.setText(errorMsg);
            emaillogin.setBackground(errorField);
            emaillogin.setCompoundDrawablesWithIntrinsicBounds(null, null, errorImg, null);
            passwordlogin.setBackground(errorField);
            passwordlogin.setCompoundDrawablesWithIntrinsicBounds(null, null, errorImg, null);
            enableEditText();
        });
    }

    private boolean checkFields(){
        resetFields();
        if (!emaillogin.getText().toString().equals("") && !passwordlogin.getText().toString().equals("")){
            return true;
        }
        Drawable errorImg = AppCompatResources.getDrawable(LoginActivity.this, R.drawable.error_field);
        Drawable errorField = AppCompatResources.getDrawable(LoginActivity.this, R.drawable.rectangle_6_shape);
        String errorMsg = getString(R.string.please_fill_the_empty_fields_string);
        runOnUiThread(() -> {
            errorInput.setVisibility(View.VISIBLE);
            errorInput.setText(errorMsg);
            if (emaillogin.getText().toString().equals("")) {
                emaillogin.setBackground(errorField);
                emaillogin.setCompoundDrawablesWithIntrinsicBounds(null, null, errorImg, null);
            }
            if (passwordlogin.getText().toString().equals("")) {
                passwordlogin.setBackground(errorField);
                passwordlogin.setCompoundDrawablesWithIntrinsicBounds(null, null, errorImg, null);
            }
            enableEditText();
        });
        return false;
    }

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
                Toast.makeText(LoginActivity.this, R.string.authentication_failed,Toast.LENGTH_SHORT).show();
            }
        };
        return new BiometricPrompt(this,executor,callback);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void disableEditText() {
        signupbtn.setFocusable(false);
        signupbtn.setClickable(false);
        loginbtn.setFocusable(false);
        loginbtn.setClickable(false);
    }
    private void enableEditText() {
        signupbtn.setFocusable(true);
        signupbtn.setClickable(true);
        loginbtn.setFocusable(true);
        loginbtn.setClickable(true);
    }


    public class checkLogin extends Thread {

        void returnResult() {
            checkLogin thread = new checkLogin();
            thread.start();
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
            loadingDialog.startLoadingDialog();
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
                        loadingDialog.dismissDialog();
                        startActivity(intent);
                        finish();
                    }else{
                        loadingDialog.dismissDialog();
                        wrongInput(getString(R.string.username_or_password_not_found_string));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                    loadingDialog.dismissDialog();
                    wrongInput(getString(R.string.unexpected_error));
                    Log.e("Error: ","Unexpected Error Occurred", t);
                }
            });

        }


        public void run() {
            if (checkFields()) {
                loginUser(createRequest(emaillogin.getText().toString(),passwordlogin.getText().toString()));
                runOnUiThread(LoginActivity.this::enableEditText);
            }
        }

    }

}