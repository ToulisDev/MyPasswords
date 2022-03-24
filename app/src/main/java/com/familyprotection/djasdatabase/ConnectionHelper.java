package com.familyprotection.djasdatabase;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {


    private Connection connection;
    private String ipAddress = "djasserver.ddns.net:1433";
    private String database = "pass";
    public static String username,password;

    public Connection conClass(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectUrl = null;

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectUrl = "jdbc:jtds:sqlserver://" +ipAddress+"/"+database+";user="+username+";password="+password+";";
            connection = DriverManager.getConnection(connectUrl);
        }catch(Exception e){
            Log.e("Error: ", e.getMessage());
        }
        return connection;
    }


}
