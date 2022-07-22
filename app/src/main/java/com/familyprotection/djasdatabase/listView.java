package com.familyprotection.djasdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.familyprotection.djasdatabase.Models.PasswordResponse;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class listView extends AppCompatActivity {

    Dialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        String username = getIntent().getStringExtra("Username");
        String password = getIntent().getStringExtra("Password");
        saveCreds(username,password);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        ListView lv = findViewById(R.id.listview);
        Button addBtn = findViewById(R.id.btn_add);
        myDialog = new Dialog(listView.this);

        lv.setOnItemClickListener(onListClick);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getData();
            swipeRefreshLayout.setRefreshing(false);
        });

        addBtn.setOnClickListener((view) -> {
            Intent intent = new Intent(listView.this, addItem.class);
            startActivity(intent);
        });
        getData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void showPopup(String tv_site, String et_username, String et_password, String tv_passId){
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setContentView(R.layout.selected_item_popup);
        Button backBtn = myDialog.findViewById(R.id.popup_back);
        Button editBtn = myDialog.findViewById(R.id.popup_edit);
        Button copyBtn = myDialog.findViewById(R.id.popup_copy_clip);
        Button showPassBtn = myDialog.findViewById(R.id.popup_show_pass);
        TextView title = myDialog.findViewById(R.id.popup_title);
        TextView username = myDialog.findViewById(R.id.popup_username);
        TextView password = myDialog.findViewById(R.id.popup_password);
        String passwordAst = getString(R.string.asterisk_pass);
        TextView id = myDialog.findViewById(R.id.popup_id);
        title.setText(tv_site);
        username.setText(et_username);
        id.setText(tv_passId);
        backBtn.setOnClickListener(v1 -> myDialog.dismiss());
        editBtn.setOnClickListener(v12 -> {
            myDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(),selectedItem.class);
            intent.putExtra("Site",tv_site);
            intent.putExtra("Username",et_username);
            intent.putExtra("Password",et_password);
            intent.putExtra("PassId",tv_passId);
            startActivity(intent);
        });
        copyBtn.setOnClickListener(v123 ->{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Username", username.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(listView.this, "Username Copied to Clipboard", Toast.LENGTH_SHORT).show();
        });
        showPassBtn.setOnClickListener(v1234 ->{
            if(password.getText().equals(passwordAst)){
                password.setText(et_password);
                runOnUiThread(()->showPassBtn.setBackgroundResource(R.drawable.ripple_effect_hide_password_button));
            }else{
                password.setText(passwordAst);
                runOnUiThread(()->showPassBtn.setBackgroundResource(R.drawable.ripple_effect_show_pass_button));
            }
        });

        myDialog.show();
    }

    private final AdapterView.OnItemClickListener onListClick = (parent, view, position, id) -> {
            String tv_site = ((TextView) view.findViewById(R.id.tv_site)).getText().toString();
            String et_username = ((TextView) view.findViewById(R.id.tv_username)).getText().toString();
            String et_password = ((TextView) view.findViewById(R.id.tv_pass)).getText().toString();
            String tv_passId = ((TextView) view.findViewById(R.id.tv_passId)).getText().toString();
            showPopup(tv_site,et_username,et_password,tv_passId);
    };

    private void saveCreds(String username, String password){
        SharedPreferences sharedPreferences = getSharedPreferences("creds",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!sharedPreferences.contains("username") || !sharedPreferences.contains("password")){
            editor.putString("username", username);
            editor.putString("password", password);
            editor.apply();
        }
    }


    SimpleAdapter ad;
    private void getData(){

        ListView lstview = findViewById(R.id.listview);

        List<PasswordResponse> MyDataList;
        ListItem thread = new ListItem();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Interrupted Error","Thread Was Interrupted unexpectedly",e);
        }
        if(ListItem.data != null) {
            MyDataList = ListItem.data;
            List<Map<String, String>> dataListToShow;
            dataListToShow = new ArrayList<>();
            for (int i = 0; i < MyDataList.size(); i++) {
                Map<String, String> tempPassword = new HashMap<>();
                tempPassword.put("tv_site", MyDataList.get(i).getPasswordsSite());
                tempPassword.put("tv_username", MyDataList.get(i).getPasswordsUsername());
                tempPassword.put("tv_pass", MyDataList.get(i).getPasswordsPassword());
                tempPassword.put("tv_passId", MyDataList.get(i).getPasswordsId());
                dataListToShow.add(tempPassword);
            }
            String[] fromw = {"tv_site", "tv_username", "tv_pass", "tv_passId"};
            int[] tow = {R.id.tv_site, R.id.tv_username, R.id.tv_pass, R.id.tv_passId};
            ad = new SimpleAdapter(listView.this, dataListToShow, R.layout.list_item, fromw, tow);
            lstview.setAdapter(ad);
        }
    }



}