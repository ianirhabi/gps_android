package com.example.irhabi_ecsboard.sendbird.service;

/**
 * Created by Programmer Jalanan on 27/July/2018
 */

import com.example.irhabi_ecsboard.sendbird.model.Anggota;
import com.example.irhabi_ecsboard.sendbird.model.Iuran;
import com.example.irhabi_ecsboard.sendbird.model.Login;
import com.example.irhabi_ecsboard.sendbird.model.ResponseIuran;
import com.example.irhabi_ecsboard.sendbird.model.Smsdata;
import com.example.irhabi_ecsboard.sendbird.model.User;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public  interface Router {

    @POST("user/regis/{imei}/{status}")
    Call<User> Postregis(@Body User r, @Path("imei") String imei, @Path ("status") String status);

    @POST("user/login")
    Call<Login> Postlogin(@Body Login L);

    @GET("user")
    Call<Anggota> Get();

    @POST("iuran")
    Call<Iuran> PostiIuran(@Body Iuran iuran);

    @POST("sms/{username}")
    Call<Smsdata> Postsms(@Body Smsdata a,
                          @Path("username") String username);

    @GET("iuran/{bulan}/{tahun}/anggota/{userid}/{minggu}")
    Call<ResponseIuran> Getiuran(@Path("bulan") String bulan, @Path("tahun")
            String tahun, @Path("userid")int userid,
                                       @Path("minggu")String minggu);
}
