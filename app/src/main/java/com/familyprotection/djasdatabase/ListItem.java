package com.familyprotection.djasdatabase;

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
        List<Map<String,String>> data;
        data = new ArrayList<>();
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.conClass();
            if (connect != null) {
                String query = "SELECT * FROM [pass].[dbo].[credentials]";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while(resultSet.next()){
                    Map<String,String> dtName = new HashMap<>();
                    dtName.put("tv_site",resultSet.getString("Site"));
                    dtName.put("tv_username",resultSet.getString("Username"));
                    dtName.put("tv_pass",resultSet.getString("Password"));
                    data.add(dtName);
                    }
                connect.close();
            }
        }catch (SQLException e){
                e.printStackTrace();
        }
        return data;
    }



}
