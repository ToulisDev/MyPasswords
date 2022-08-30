package com.familyprotection.djasdatabase.Models;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PasswordService {

    @POST("passwordApp/api/Passwords/GetPasswords")
    Call<List<PasswordResponse>> getPasswords(@Header("Authorization") String authorization);
    @POST("passwordApp/api/Passwords/DeletePassword")
    Call<String> deletePassword(@Header("Authorization") String authorization, @Query("passwordId") String passwordId);
    @POST("passwordApp/api/Passwords/UpdatePassword")
    Call<String> updatePassword(@Header("Authorization") String authorization, @Query("passId") String passId, @Body PasswordRequest passwordRequest);
    @POST("passwordApp/api/Passwords/CreatePassword")
    Call<String> createPassword(@Header("Authorization") String authorization, @Body PasswordRequest passwordRequest);
}
