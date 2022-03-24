package com.familyprotection.djasdatabase;

import android.content.Intent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItem {

    public List<Map<String,String>>getlist(){
        List<Map<String,String>> data = null;
        data = new ArrayList<Map<String,String>>();
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.conClass();
            if (connect != null) {
                String querry = "SELECT * FROM [pass].[dbo].[credentials]";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(querry);
                while(resultSet.next()){
                    Map<String,String> dtname = new HashMap<String,String>();
                    dtname.put("tv_site",resultSet.getString("Site"));
                    dtname.put("tv_username",resultSet.getString("Username"));
                    dtname.put("tv_pass",resultSet.getString("Password"));
                    data.add(dtname);
                    }
                connect.close();
            }
        }catch (SQLException throwables){
                throwables.printStackTrace();
        }
        return data;
    }



}
