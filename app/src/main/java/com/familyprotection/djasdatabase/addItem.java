package com.familyprotection.djasdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class addItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Button backBtn = (Button) findViewById(R.id.btn_back);
        Button createBtn = (Button) findViewById(R.id.btn_create);
        EditText et_title = (EditText) findViewById(R.id.et_title);
        EditText et_username = (EditText) findViewById(R.id.et_email);
        EditText et_password = (EditText) findViewById(R.id.et_pass);

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
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.conClass();
            if (connect != null) {
                String query = "IF NOT EXISTS ( SELECT * FROM [pass].[dbo].[credentials]\n" +
                        "                   WHERE Site = '"+title+"')\n" +
                        "\t\t\t\t   BEGIN\n" +
                        "\t\t\t\t\t   INSERT INTO [pass].[dbo].[credentials] (Site, Username, Password) VALUES ('"+title+"', '"+username+"', '"+password+"')\n" +
                        "\t\t\t\t   END\n" +
                        "\t\t\t\t   ELSE\n" +
                        "\t\t\t\t   THROW 51000, 'The Record Exists', 1;";
                Statement statement = connect.createStatement();
                int resultSet = statement.executeUpdate(query);
                Log.e("INFO",String.valueOf(resultSet));
                connect.close();
                finish();
            }
        }catch (SQLException e){
            Log.e("ERROR",e.getMessage());
            Toast.makeText(addItem.this,"Άλλαξε το "+title+" καθώς υπάρχει ήδη.",Toast.LENGTH_LONG).show();
        }
    }
}