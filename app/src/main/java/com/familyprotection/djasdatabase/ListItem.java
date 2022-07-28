package com.familyprotection.djasdatabase;

import android.util.Log;

import com.familyprotection.djasdatabase.Models.ConnectionHelper;
import com.familyprotection.djasdatabase.Models.PasswordResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ListItem extends Thread {
    public static List<PasswordResponse> data;
    private boolean isAuthorized = true;

    public boolean getAuthorized(){
        return this.isAuthorized;
    }

    public void setAuthorized(Boolean authorized){
        this.isAuthorized = authorized;
    }

    public void getlist(){
        String token = "Bearer "+ConnectionHelper.token;
        Call<List<PasswordResponse>> passwordResponseCall = ConnectionHelper.getPasswordService().getPasswords(token);
        try {
            Response<List<PasswordResponse>> response = passwordResponseCall.execute();
            if(response.isSuccessful())
                data = response.body();
            else
                setAuthorized(false);

        }catch (Exception e){
            Log.e("Error:","An error Occurred while executing request",e);
        }
    }

    @Override
    public void run() {
        getlist();
    }
}
