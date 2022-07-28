package com.familyprotection.djasdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.io.FileOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addItem extends AppCompatActivity {

    LoadingDialog loadingDialog;
    RecyclerView imgList;
    ArrayList<Bitmap> bitmapList = new ArrayList<>();
    BitmapAdapter adapter;
    Bitmap selectedLogo;
    EditText et_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Button backBtn = findViewById(R.id.btn_back);
        Button createBtn = findViewById(R.id.btn_create);
        et_title = findViewById(R.id.et_title);
        EditText et_username = findViewById(R.id.et_email);
        EditText et_password = findViewById(R.id.et_pass);
        EditText et_website = findViewById(R.id.et_website);

        imgList = findViewById(R.id.imgList);
        adapter = new BitmapAdapter(bitmapList, this::onListClick);
        LinearLayoutManager bLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        bLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imgList.setLayoutManager(bLinearLayoutManager);
        imgList.setAdapter(adapter);
        loadingDialog = new LoadingDialog(addItem.this);

        backBtn.setOnClickListener(v -> finish());

        createBtn.setOnClickListener(v -> createBtnMethod(et_title,et_username,et_password,et_website));
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
                selectedLogo = bitmapList.get(0);
                adapter.previous_position = 0;
                adapter.selected_position = 0;
            }else {
                bitmapList.clear();
            }
            adapter.notifyDataSetChanged();
        });
        googleList.start();
    }

    private void createBtnMethod(EditText et_title, EditText et_username, EditText et_password, EditText et_website){
        String title = et_title.getText().toString();
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        String website = et_website.getText().toString();
        if(website.equals(""))
            website = "www."+title+".com";
        if(!title.equals("") && !username.equals("") && !password.equals(""))
            createData(title,username,password,website);
        else
            Toast.makeText(addItem.this,R.string.fill_all_fields,Toast.LENGTH_LONG).show();
    }

    private void createData(String title,String username,String password, String website){
        loadingDialog.startLoadingDialog();
        String token = "Bearer "+ ConnectionHelper.token;
        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setPasswordsSite(title);
        passwordRequest.setPasswordsUsername(username);
        passwordRequest.setPasswordsPassword(password);
        passwordRequest.setPasswordsWebsite(website);
        Call<String> passwordResponseCall = ConnectionHelper.getPasswordService().createPassword(token,passwordRequest);
        passwordResponseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()) {
                    loadingDialog.dismissDialog();
                    createFile();
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

    private void onListClick(Bitmap image, int position){
        if(adapter.previous_position == -1){
            adapter.previous_position = position;
        }else{
            adapter.previous_position = adapter.selected_position;
        }
        adapter.selected_position = position;
        selectedLogo = image;
        adapter.notifyItemChanged(adapter.previous_position);
        adapter.notifyItemChanged(adapter.selected_position);
    }

    private void createFile(){
        String filename = "___"+et_title.getText().toString();
        try {
            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            selectedLogo.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            Log.i("SUCCESSFUL","FILE SUCCESSFULLY CREATED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}