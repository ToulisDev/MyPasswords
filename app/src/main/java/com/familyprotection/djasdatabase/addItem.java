package com.familyprotection.djasdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.familyprotection.djasdatabase.Models.BitmapAdapter;
import com.familyprotection.djasdatabase.Models.ConnectionHelper;
import com.familyprotection.djasdatabase.Models.PasswordRequest;
import com.familyprotection.djasdatabase.Models.googleRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addItem extends AppCompatActivity {

    LoadingDialog loadingDialog;
    RecyclerView imgList;
    ArrayList<Bitmap> bitmapList = new ArrayList<>();
    BitmapAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Button backBtn = findViewById(R.id.btn_back);
        Button createBtn = findViewById(R.id.btn_create);
        EditText et_title = findViewById(R.id.et_title);
        EditText et_username = findViewById(R.id.et_email);
        EditText et_password = findViewById(R.id.et_pass);

        imgList = findViewById(R.id.imgList);
        adapter = new BitmapAdapter(bitmapList);
        LinearLayoutManager bLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        bLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imgList.setLayoutManager(bLinearLayoutManager);
        imgList.setItemAnimator(new DefaultItemAnimator());
        imgList.setAdapter(adapter);
        loadingDialog = new LoadingDialog(addItem.this);

        backBtn.setOnClickListener(v -> finish());

        createBtn.setOnClickListener(v -> createBtnMethod(et_title,et_username,et_password));
        et_title.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                getImageLogo(et_title.getText().toString());
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getImageLogo(String serviceName){
        googleRequest googleList = new googleRequest(serviceName, images -> {
            if(images != null) {
                bitmapList.clear();
                bitmapList.addAll(images);
            }else {
                bitmapList.clear();
            }
            adapter.notifyDataSetChanged();
        });
        googleList.start();


    }

    private void createBtnMethod(EditText et_title, EditText et_username, EditText et_password){
        String title = et_title.getText().toString();
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        if(!title.equals("") && !username.equals("") && !password.equals(""))
            createData(title,username,password);
        else
            Toast.makeText(addItem.this,R.string.fill_all_fields,Toast.LENGTH_LONG).show();
    }

    private void createData(String title,String username,String password){
        loadingDialog.startLoadingDialog();
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
                    loadingDialog.dismissDialog();
                    finish();
                }else{
                    loadingDialog.dismissDialog();
                    runOnUiThread(() -> Toast.makeText(addItem.this, R.string.failed_to_create_pass, Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                loadingDialog.dismissDialog();
                Log.e("Failed to Request","An unexpected Error occurred while executing Request",t);
                runOnUiThread(() -> Toast.makeText(addItem.this, R.string.unexpected_error, Toast.LENGTH_LONG).show());
            }
        });
    }
}