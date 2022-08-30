package com.familyprotection.djasdatabase.Models;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("passwordApp/auth/Auth/register/")
    Call<String> registerUser(@Body UserRequest userRequest);

    @POST("passwordApp/auth/Auth/login/")
    Call<UserResponse> loginUser(@Body UserRequest userRequest);
}
