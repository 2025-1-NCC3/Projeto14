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

    @POST("updateUserDriver")
    Call<User> updateUserDriver(@Body User user);

    @POST("updateUserPassenger")
    Call<User> updateUserPassenger(@Body User user);

    @POST("deleteUserDriver")
    Call<User> deleteUserDriver(@Body User user);

    @POST("deleteUserPassenger")
    Call<User> deleteUserPassenger(@Body User user);
}
