package com.familyprotection.djasdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

public class listView extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        saveCreds();
        getData();

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        ListView lv = (ListView) findViewById(R.id.listview);
        Button addBtn = (Button) findViewById(R.id.btn_add);

        lv.setOnItemClickListener(onListClick);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getData();
            swipeRefreshLayout.setRefreshing(false);
        });

        addBtn.setOnClickListener((view) -> {
            Intent intent = new Intent(listView.this, addItem.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private final AdapterView.OnItemClickListener onListClick = (parent, view, position, id) -> {
            String tv_site = ((TextView) view.findViewById(R.id.tv_site)).getText().toString();
            String et_username = ((TextView) view.findViewById(R.id.tv_username)).getText().toString();
            String et_password = ((TextView) view.findViewById(R.id.tv_pass)).getText().toString();
            Intent intent = new Intent(getApplicationContext(),selectedItem.class);
            intent.putExtra("Site",tv_site);
            intent.putExtra("Username",et_username);
            intent.putExtra("Password",et_password);
            startActivity(intent);

    };

    private void saveCreds(){
        SharedPreferences sharedPreferences = getSharedPreferences("creds",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!sharedPreferences.contains("username") || !sharedPreferences.contains("password")){
            editor.putString("username", ConnectionHelper.username);
            editor.putString("password", ConnectionHelper.password);
            editor.apply();
        }
    }


    SimpleAdapter ad;
    private void getData(){

        ListView lstview = (ListView) findViewById(R.id.listview);

        List<Map<String,String>> MyDataList;
        ListItem MyData = new ListItem();
        MyDataList = MyData.getlist();

        String[] fromw = {"tv_site","tv_username","tv_pass"};
        int[] tow = {R.id.tv_site,R.id.tv_username,R.id.tv_pass};
        ad = new SimpleAdapter(listView.this,MyDataList,R.layout.list_item,fromw,tow);
        lstview.setAdapter(ad);
    }



}