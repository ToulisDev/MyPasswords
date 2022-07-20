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
                Toast.makeText(selectedItem.this,"Username και Password δεν μπορούν να είναι κενά.",Toast.LENGTH_LONG).show();
        });

        deleteBtn.setOnClickListener((view) -> deleteData(tvTitle.getText().toString(),tvPassId.getText().toString()));


    }

    private void deleteData(String site, String passId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Επιβεβαίωση");
        builder.setMessage("Διαγραφή των κωδικών του \""+site+"\"");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Ναι", (dialog, which) -> {
            String token = "Bearer "+ConnectionHelper.token;
            Call<String> passwordResponseCall = ConnectionHelper.getPasswordService().deletePassword(token,passId);
            passwordResponseCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if(response.isSuccessful()) {
                        finish();
                    }else{
                        runOnUiThread(() -> Toast.makeText(selectedItem.this, "Failed to Delete Password", Toast.LENGTH_LONG).show());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.e("Failed to Request","An unexpected Error occurred while executing Request",t);
                    runOnUiThread(() -> Toast.makeText(selectedItem.this, "An Unexpected Error Occurred", Toast.LENGTH_LONG).show());
                }
            });
        });

        builder.setNegativeButton("Όχι", (dialog, which) -> {

            // Do nothing
            dialog.dismiss();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void editData(String site, String username, String password, String passId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Επιβεβαίωση");
        builder.setMessage("Ενημέρωση των κωδικών του \""+site+"\"");
        builder.setIcon(android.R.drawable.ic_dialog_info);


        builder.setPositiveButton("Ναι", (dialog, which) -> {
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
                        finish();
                    }else{
                        runOnUiThread(() -> Toast.makeText(selectedItem.this, "Failed to Update Password", Toast.LENGTH_LONG).show());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.e("Failed to Request","An unexpected Error occurred while executing Request",t);
                    runOnUiThread(() -> Toast.makeText(selectedItem.this, "An Unexpected Error Occurred", Toast.LENGTH_LONG).show());
                }
            });
        });

        builder.setNegativeButton("Όχι", (dialog, which) -> {

            // Do nothing
            dialog.dismiss();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}