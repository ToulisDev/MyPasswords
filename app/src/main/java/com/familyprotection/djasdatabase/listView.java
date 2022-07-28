package com.familyprotection.djasdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.familyprotection.djasdatabase.Models.PasswordAdapter;
import com.familyprotection.djasdatabase.Models.PasswordResponse;


import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class listView extends AppCompatActivity {

    Dialog myDialog;
    LoadingDialog loadingDialog;
    ListView lstview;
    SwipeRefreshLayout swipeRefreshLayout;
    Button addBtn;
    PasswordAdapter adapter;
    List<PasswordResponse> listPasswordResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        String username = getIntent().getStringExtra("Username");
        String password = getIntent().getStringExtra("Password");
        saveCreds(username,password);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        lstview = findViewById(R.id.listview);
        lstview.setOnItemClickListener(onListClick);
        listPasswordResponse = new ArrayList<>();
        adapter = new PasswordAdapter(this, listPasswordResponse);
        lstview.setAdapter(adapter);
        lstview.setEmptyView(findViewById(R.id.listIsEmpty));
        addBtn = findViewById(R.id.btn_add);
        myDialog = new Dialog(listView.this);
        loadingDialog = new LoadingDialog(listView.this);
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

    private void showPopup(String tv_site, String et_username, String et_password, String tv_passId, String et_website, String tv_insertDate){
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setContentView(R.layout.selected_item_popup);
        Button backBtn = myDialog.findViewById(R.id.popup_back);
        Button editBtn = myDialog.findViewById(R.id.popup_edit);
        Button copyBtn = myDialog.findViewById(R.id.popup_copy_clip);
        Button showPassBtn = myDialog.findViewById(R.id.popup_show_pass);
        TextView title = myDialog.findViewById(R.id.popup_title);
        TextView username = myDialog.findViewById(R.id.popup_username);
        TextView password = myDialog.findViewById(R.id.popup_password);
        TextView website = myDialog.findViewById(R.id.popup_website);
        TextView insertDate = myDialog.findViewById(R.id.popup_date);
        ImageView logo = myDialog.findViewById(R.id.popup_logo);
        String passwordAst = getString(R.string.asterisk_pass);
        TextView id = myDialog.findViewById(R.id.popup_id);
        title.setText(tv_site);
        username.setText(et_username);
        id.setText(tv_passId);
        website.setText(et_website);
        insertDate.setText(tv_insertDate);
        setLogo(logo,tv_site);
        backBtn.setOnClickListener(v1 -> myDialog.dismiss());
        editBtn.setOnClickListener(v12 -> {
            myDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(),selectedItem.class);
            intent.putExtra("Site",tv_site);
            intent.putExtra("Username",et_username);
            intent.putExtra("Password",et_password);
            intent.putExtra("PassId",tv_passId);
            intent.putExtra("Website",et_website);
            startActivity(intent);
        });
        copyBtn.setOnClickListener(v123 ->{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Username", username.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(listView.this, R.string.username_copy_clip, Toast.LENGTH_SHORT).show();
        });
        showPassBtn.setOnClickListener(v1234 ->{
            if(password.getText().equals(passwordAst)){
                runOnUiThread(()-> {
                    password.setText(et_password);
                    showPassBtn.setBackgroundResource(R.drawable.ripple_effect_hide_password_button);
                });
            }else{
                runOnUiThread(()->{
                    showPassBtn.setBackgroundResource(R.drawable.ripple_effect_show_pass_button);
                    password.setText(passwordAst);
                });
            }
        });

        myDialog.show();
    }

    private final AdapterView.OnItemClickListener onListClick = (parent, view, position, id) -> {
            String tv_site = ((TextView) view.findViewById(R.id.tv_site)).getText().toString();
            String et_username = ((TextView) view.findViewById(R.id.tv_username)).getText().toString();
            String et_password = ((TextView) view.findViewById(R.id.tv_pass)).getText().toString();
            String tv_passId = ((TextView) view.findViewById(R.id.tv_passId)).getText().toString();
            String et_website = ((TextView) view.findViewById(R.id.tv_website)).getText().toString();
            String tv_insertDate = ((TextView) view.findViewById(R.id.tv_insertDate)).getText().toString();
            showPopup(tv_site,et_username,et_password,tv_passId,et_website,tv_insertDate);
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



    private void getData(){
        loadingDialog.startLoadingDialog();
        List<PasswordResponse> MyDataList;
        ListItem thread = new ListItem();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Interrupted Error","Thread Was Interrupted unexpectedly",e);
        }
        if(!thread.getAuthorized()){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if(ListItem.data != null) {
            MyDataList = ListItem.data;
            adapter.clear();
            adapter.addAll(MyDataList);
        }else{
            adapter.clear();
        }
        loadingDialog.dismissDialog();
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

}