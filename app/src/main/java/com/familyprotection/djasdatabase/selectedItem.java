package com.familyprotection.djasdatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class selectedItem extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);

        String site = getIntent().getStringExtra("Site").toString();
        String username = getIntent().getStringExtra("Username").toString();
        String password = getIntent().getStringExtra("Password").toString();

        TextView tvTitle = (TextView) findViewById(R.id.tv_site);
        EditText et_username = (EditText) findViewById(R.id.et_email);
        EditText et_password = (EditText) findViewById(R.id.et_pass);

        Button backBtn = (Button) findViewById(R.id.btn_back);
        Button deleteBtn = (Button) findViewById(R.id.btn_delete);
        Button editBtn = (Button) findViewById(R.id.btn_edit);

        tvTitle.setText(site);
        et_username.setText(username);
        et_password.setText(password);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_username.getText().toString().equals("") && !et_password.getText().toString().equals(""))
                    editData(tvTitle.getText().toString(),et_username.getText().toString(),et_password.getText().toString());
                else
                    Toast.makeText(selectedItem.this,"Username και Password δεν μπορούν να είναι κενά.",Toast.LENGTH_LONG).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData(tvTitle.getText().toString());
            }
        });


    }

    private void deleteData(String site){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Επιβεβαίωση");
        builder.setMessage("Διαγραφή των κωδικών του \""+site+"\"");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                try{
                    ConnectionHelper connectionHelper = new ConnectionHelper();
                    Connection connect = connectionHelper.conClass();
                    if (connect != null) {
                        String querry = "DELETE FROM [pass].[dbo].[credentials] WHERE Site = '"+site+"'";
                        Statement statement = connect.createStatement();
                        int resultSet = statement.executeUpdate(querry);
                        connect.close();
                    }
                }catch (SQLException throwables){
                    Log.e("ERROR",throwables.getMessage());
                }
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("Όχι", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void editData(String site, String username, String password) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Επιβεβαίωση");
        builder.setMessage("Ενημέρωση των κωδικών του \""+site+"\"");
        builder.setIcon(android.R.drawable.ic_dialog_info);


        builder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                try{
                    ConnectionHelper connectionHelper = new ConnectionHelper();
                    Connection connect = connectionHelper.conClass();
                    if (connect != null) {
                        String querry = "UPDATE [pass].[dbo].[credentials] SET Username = '"+username+"', Password = '"+password+"' WHERE Site = '"+site+"'";
                        Statement statement = connect.createStatement();
                        int resultSet = statement.executeUpdate(querry);
                        connect.close();
                    }
                }catch (SQLException throwables){
                    Log.e("ERROR",throwables.getMessage());
                }
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("Όχι", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}