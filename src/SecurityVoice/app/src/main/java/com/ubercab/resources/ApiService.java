package com.ubercab.resources;

import com.ubercab.entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("loginUser")
    Call<User> loginUser(@Body User user);
    @POST("registerDriver")
    Call<User> registerDriver(@Body User user);
    @POST("registerPassenger")
    Call<User> registerPassenger(@Body User user);
}
