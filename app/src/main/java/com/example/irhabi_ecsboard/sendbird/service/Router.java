package com.example.irhabi_ecsboard.sendbird.service;



import com.example.irhabi_ecsboard.sendbird.model.Anggota;
import com.example.irhabi_ecsboard.sendbird.model.Login;
import com.example.irhabi_ecsboard.sendbird.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public  interface Router {

    @POST("user/regis/{imei}")
    Call<User> Postregis(@Body User r, @Path("imei") String imei);

    @POST("user/login")
    Call<Login> Postlogin(@Body Login L);

    @GET("user")
    Call<Anggota> Get();
}
