package com.familyprotection.djasdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.familyprotection.djasdatabase.Models.ConnectionHelper;
import com.familyprotection.djasdatabase.Models.PasswordRequest;

import org.jetbrains.annotations.Nullable;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class selectedItem extends AppCompatActivity {

    LoadingDialog loadingDialog;
    ImageView passLogo;
    String site;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);

        site = getIntent().getStringExtra("Site");
        String username = getIntent().getStringExtra("Username");
        String password = getIntent().getStringExtra("Password");
        String passId = getIntent().getStringExtra("PassId");
        String website = getIntent().getStringExtra("Website");

        TextView tvTitle = findViewById(R.id.tv_site);
        TextView tvPassId = findViewById(R.id.si_passId);
        EditText et_username = findViewById(R.id.et_email);
        EditText et_password = findViewById(R.id.et_pass);
        EditText et_website = findViewById(R.id.et_website);
        passLogo = findViewById(R.id.pass_image);
        setLogo(passLogo,site);
        loadingDialog = new LoadingDialog(selectedItem.this);

        Button backBtn = findViewById(R.id.btn_back);
        Button deleteBtn = findViewById(R.id.btn_delete);
        Button editBtn = findViewById(R.id.btn_edit);

        tvTitle.setText(site);
        et_username.setText(username);
        et_password.setText(password);
        tvPassId.setText(passId);
        et_website.setText(website);

        backBtn.setOnClickListener(view -> finish());

        editBtn.setOnClickListener((view) -> {
            if(!et_username.getText().toString().equals("") && !et_password.getText().toString().equals("") && !et_website.getText().toString().equals(""))
                editData(tvTitle.getText().toString(),et_username.getText().toString(),et_password.getText().toString(),tvPassId.getText().toString(), et_website.getText().toString());
            else
                Toast.makeText(selectedItem.this,R.string.fill_all_fields,Toast.LENGTH_LONG).show();
        });

        deleteBtn.setOnClickListener((view) -> deleteData(tvTitle.getText().toString(),tvPassId.getText().toString()));


    }

    private void deleteData(String site, String passId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.confirmation);
        builder.setMessage(getString(R.string.delete_fields_from)+" \""+site+"\"");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            loadingDialog.startLoadingDialog();
            String token = "Bearer "+ConnectionHelper.token;
            Call<String> passwordResponseCall = ConnectionHelper.getPasswordService().deletePassword(token,passId);
            passwordResponseCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if(response.isSuccessful()) {
                        loadingDialog.dismissDialog();
                        deleteLogoFromFiles();
                        finish();
                    }else{
                        loadingDialog.dismissDialog();
                        runOnUiThread(() -> Toast.makeText(selectedItem.this, R.string.failed_to_delete, Toast.LENGTH_LONG).show());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    loadingDialog.dismissDialog();
                    Log.e("Failed to Request","An unexpected Error occurred while executing Request",t);
                    runOnUiThread(() -> Toast.makeText(selectedItem.this, R.string.unexpected_error, Toast.LENGTH_LONG).show());
                }
            });
        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> {

            // Do nothing
            dialog.dismiss();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void editData(String site, String username, String password, String passId, String website) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.confirmation);
        builder.setMessage(getString(R.string.update_fields_for)+" \""+site+"\"");
        builder.setIcon(android.R.drawable.ic_dialog_info);


        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            loadingDialog.startLoadingDialog();
            String token = "Bearer "+ConnectionHelper.token;
            PasswordRequest passwordRequest = new PasswordRequest();
            passwordRequest.setPasswordsSite(site);
            passwordRequest.setPasswordsUsername(username);
            passwordRequest.setPasswordsPassword(password);
            passwordRequest.setPasswordsWebsite(website);
            Call<String> passwordResponseCall = ConnectionHelper.getPasswordService().updatePassword(token,passId,passwordRequest);
            passwordResponseCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if(response.isSuccessful()) {
                        loadingDialog.dismissDialog();
                        editLogoFromFiles(passwordRequest.getPasswordsSite());
                        finish();
                    }else{
                        loadingDialog.dismissDialog();
                        runOnUiThread(() -> Toast.makeText(selectedItem.this, R.string.failed_to_update, Toast.LENGTH_LONG).show());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    loadingDialog.dismissDialog();
                    Log.e("Failed to Request","An unexpected Error occurred while executing Request",t);
                    runOnUiThread(() -> Toast.makeText(selectedItem.this, R.string.unexpected_error, Toast.LENGTH_LONG).show());
                }
            });
        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> {

            // Do nothing
            dialog.dismiss();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void setLogo(ImageView logo, String Site){
        String logoName = "___" + Site;
        File file = getAppSpecificFile(this, logoName);
        if (file != null) {
            if(file.exists()) {
                String path = file.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                logo.setImageBitmap(bitmap);
            }else
                logo.setBackground(AppCompatResources.getDrawable(this, R.drawable.default_logo));
        }else{
            logo.setBackground(AppCompatResources.getDrawable(this, R.drawable.default_logo));
        }
    }
    @Nullable
    File getAppSpecificFile(Context context, String filename) {
        return new File(context.getFilesDir(), filename);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    void deleteLogoFromFiles(){
        String logoName = "___" + site;
        File file = getAppSpecificFile(this, logoName);
        try {
            if (file != null && file.exists()) {
                file.delete();
            }
        }catch (Exception e){
            Log.e("Deletion Failed!", "Couldn't Delete Logo image",e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    void editLogoFromFiles(String newName){
        String logoName = "___"+site;
        String newLogoName = "___"+newName;
        try {
            File file = getAppSpecificFile(this, logoName);
            File newFile = getAppSpecificFile(this, newLogoName);
            if (file != null) {
                if (newFile != null) {
                    file.renameTo(newFile);
                }
            }
        }catch (Exception e){
            Log.e("Rename Failed","Rename of Previous File Failed");
        }
    }
}