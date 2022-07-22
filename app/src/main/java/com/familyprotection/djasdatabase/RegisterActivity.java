package com.familyprotection.djasdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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


    //Helpful Methods
    private void resetFields(){
        Drawable defaultField = AppCompatResources.getDrawable(RegisterActivity.this, R.drawable.rectangle_1_shape);
        runOnUiThread(() -> {
            responseMsg.setVisibility(View.INVISIBLE);
            emailET.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            emailET.setBackground(defaultField);
            passwordET.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            passwordET.setBackground(defaultField);
            confirmPassET.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            confirmPassET.setBackground(defaultField);
        });
    }
    private void wrongInput(String errorMsg){
        resetFields();
        Drawable errorImg = AppCompatResources.getDrawable(RegisterActivity.this, R.drawable.error_field);
        Drawable errorField = AppCompatResources.getDrawable(RegisterActivity.this, R.drawable.rectangle_6_shape);
        runOnUiThread(() -> {
            responseMsg.setVisibility(View.VISIBLE);
            responseMsg.setText(errorMsg);
            emailET.setBackground(errorField);
            emailET.setCompoundDrawablesWithIntrinsicBounds(null, null, errorImg, null);
            enableEditText();
        });
    }

    private boolean checkFields(){
        resetFields();
        if (!emailET.getText().toString().equals("") && !passwordET.getText().toString().equals("")){
            if(passwordET.getText().toString().equals(confirmPassET.getText().toString())) {
                return true;
            }
        }
        Drawable errorImg = AppCompatResources.getDrawable(RegisterActivity.this, R.drawable.error_field);
        Drawable errorField = AppCompatResources.getDrawable(RegisterActivity.this, R.drawable.rectangle_6_shape);
        String errorMsg = getString(R.string.please_fill_the_empty_fields_string);
        String matchErrorMsg = getString(R.string.password_does_not_match_string);
        runOnUiThread(() -> {
            responseMsg.setVisibility(View.VISIBLE);
            if (emailET.getText().toString().equals("")) {
                emailET.setBackground(errorField);
                emailET.setCompoundDrawablesWithIntrinsicBounds(null, null, errorImg, null);
            }
            if (passwordET.getText().toString().equals("")) {
                passwordET.setBackground(errorField);
                passwordET.setCompoundDrawablesWithIntrinsicBounds(null, null, errorImg, null);
            }
            if (confirmPassET.getText().toString().equals("")) {
                confirmPassET.setBackground(errorField);
                confirmPassET.setCompoundDrawablesWithIntrinsicBounds(null, null, errorImg, null);
            }
            if(emailET.getText().toString().equals("") || passwordET.getText().toString().equals("") || confirmPassET.getText().toString().equals("")){
                responseMsg.setText(errorMsg);
            }else{
                responseMsg.setText(matchErrorMsg);
            }
            enableEditText();
        });
        return false;
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

    public class registerUser extends Thread{

        void returnResult(){
            registerUser thread = new registerUser();
            thread.start();
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
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        wrongInput("User Already Exists!");
                    }
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    wrongInput("An Unexpected Error Occurred!");
                    Log.e("Error: ","Failed Sending Request!",t);
                }
            });
        }

        public void run() {
            if(checkFields()) {
                createUser(createRequest());
                runOnUiThread(RegisterActivity.this::enableEditText);
            }
        }

    }

}