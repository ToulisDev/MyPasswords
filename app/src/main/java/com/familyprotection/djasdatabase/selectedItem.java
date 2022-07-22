package com.familyprotection.djasdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.familyprotection.djasdatabase.Models.ConnectionHelper;
import com.familyprotection.djasdatabase.Models.PasswordRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class selectedItem extends AppCompatActivity {

    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);

        String site = getIntent().getStringExtra("Site");
        String username = getIntent().getStringExtra("Username");
        String password = getIntent().getStringExtra("Password");
        String passId = getIntent().getStringExtra("PassId");

        TextView tvTitle = findViewById(R.id.tv_site);
        TextView tvPassId = findViewById(R.id.si_passId);
        EditText et_username = findViewById(R.id.et_email);
        EditText et_password = findViewById(R.id.et_pass);
        loadingDialog = new LoadingDialog(selectedItem.this);

        Button backBtn = findViewById(R.id.btn_back);
        Button deleteBtn = findViewById(R.id.btn_delete);
        Button editBtn = findViewById(R.id.btn_edit);

        tvTitle.setText(site);
        et_username.setText(username);
        et_password.setText(password);
        tvPassId.setText(passId);

        backBtn.setOnClickListener(view -> finish());

        editBtn.setOnClickListener((view) -> {
            if(!et_username.getText().toString().equals("") && !et_password.getText().toString().equals(""))
                editData(tvTitle.getText().toString(),et_username.getText().toString(),et_password.getText().toString(),tvPassId.getText().toString());
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

    private void editData(String site, String username, String password, String passId) {

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
            Call<String> passwordResponseCall = ConnectionHelper.getPasswordService().updatePassword(token,passId,passwordRequest);
            passwordResponseCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if(response.isSuccessful()) {
                        loadingDialog.dismissDialog();
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


}