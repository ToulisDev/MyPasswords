package com.familyprotection.djasdatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class selectedItem extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);

        String site = getIntent().getStringExtra("Site");
        String username = getIntent().getStringExtra("Username");
        String password = getIntent().getStringExtra("Password");

        TextView tvTitle = (TextView) findViewById(R.id.tv_site);
        EditText et_username = (EditText) findViewById(R.id.et_email);
        EditText et_password = (EditText) findViewById(R.id.et_pass);

        Button backBtn = (Button) findViewById(R.id.btn_back);
        Button deleteBtn = (Button) findViewById(R.id.btn_delete);
        Button editBtn = (Button) findViewById(R.id.btn_edit);

        tvTitle.setText(site);
        et_username.setText(username);
        et_password.setText(password);

        backBtn.setOnClickListener(view -> finish());

        editBtn.setOnClickListener((view) -> {
            if(!et_username.getText().toString().equals("") && !et_password.getText().toString().equals(""))
                editData(tvTitle.getText().toString(),et_username.getText().toString(),et_password.getText().toString());
            else
                Toast.makeText(selectedItem.this,"Username και Password δεν μπορούν να είναι κενά.",Toast.LENGTH_LONG).show();
        });

        deleteBtn.setOnClickListener((view) -> deleteData(tvTitle.getText().toString()));


    }

    private void deleteData(String site){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Επιβεβαίωση");
        builder.setMessage("Διαγραφή των κωδικών του \""+site+"\"");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Ναι", (dialog, which) -> {
            // Do nothing but close the dialog
            try{
                ConnectionHelper connectionHelper = new ConnectionHelper();
                Connection connect = connectionHelper.conClass();
                if (connect != null) {
                    String query = "DELETE FROM [pass].[dbo].[credentials] WHERE Site = '"+site+"'";
                    Statement statement = connect.createStatement();
                    int resultSet = statement.executeUpdate(query);
                    Log.e("INFO", String.valueOf(resultSet));
                    connect.close();
                }
            }catch (SQLException e){
                Log.e("ERROR",e.getMessage());
            }
            dialog.dismiss();
            finish();
        });

        builder.setNegativeButton("Όχι", (dialog, which) -> {

            // Do nothing
            dialog.dismiss();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void editData(String site, String username, String password) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Επιβεβαίωση");
        builder.setMessage("Ενημέρωση των κωδικών του \""+site+"\"");
        builder.setIcon(android.R.drawable.ic_dialog_info);


        builder.setPositiveButton("Ναι", (dialog, which) -> {
            // Do nothing but close the dialog
            try{
                ConnectionHelper connectionHelper = new ConnectionHelper();
                Connection connect = connectionHelper.conClass();
                if (connect != null) {
                    String query = "UPDATE [pass].[dbo].[credentials] SET Username = '"+username+"', Password = '"+password+"' WHERE Site = '"+site+"'";
                    Statement statement = connect.createStatement();
                    int resultSet = statement.executeUpdate(query);
                    Log.e("INFO",String.valueOf(resultSet));
                    connect.close();
                }
            }catch (SQLException e){
                Log.e("ERROR",e.getMessage());
            }
            dialog.dismiss();
            finish();
        });

        builder.setNegativeButton("Όχι", (dialog, which) -> {

            // Do nothing
            dialog.dismiss();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}