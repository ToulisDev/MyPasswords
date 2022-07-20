package com.familyprotection.djasdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.familyprotection.djasdatabase.Models.ConnectionHelper;
import com.familyprotection.djasdatabase.Models.PasswordRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Button backBtn = findViewById(R.id.btn_back);
        Button createBtn = findViewById(R.id.btn_create);
        EditText et_title = findViewById(R.id.et_title);
        EditText et_username = findViewById(R.id.et_email);
        EditText et_password = findViewById(R.id.et_pass);

        backBtn.setOnClickListener(v -> finish());

        createBtn.setOnClickListener(v -> createBtnMethod(et_title,et_username,et_password));


    }
    private void createBtnMethod(EditText et_title, EditText et_username, EditText et_password){
        String title = et_title.getText().toString();
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        if(!title.equals("") && !username.equals("") && !password.equals(""))
            createData(title,username,password);
        else
            Toast.makeText(addItem.this,"Συμπλήρωσε όλα τα πεδία",Toast.LENGTH_LONG).show();
    }

    private void createData(String title,String username,String password){
        String token = "Bearer "+ ConnectionHelper.token;
        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setPasswordsSite(title);
        passwordRequest.setPasswordsUsername(username);
        passwordRequest.setPasswordsPassword(password);
        Call<String> passwordResponseCall = ConnectionHelper.getPasswordService().createPassword(token,passwordRequest);
        passwordResponseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()) {
                    finish();
                }else{
                    runOnUiThread(() -> Toast.makeText(addItem.this, "Failed to Create Password", Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("Failed to Request","An unexpected Error occurred while executing Request",t);
                runOnUiThread(() -> Toast.makeText(addItem.this, "An Unexpected Error Occurred", Toast.LENGTH_LONG).show());
            }
        });
    }
}