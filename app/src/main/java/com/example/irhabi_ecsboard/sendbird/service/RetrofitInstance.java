package com.example.irhabi_ecsboard.sendbird.service;

/**
 * Created By Programmer Jalanan on January 12/082018
 */

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.irhabi_ecsboard.sendbird.service.Config.URL;


public class RetrofitInstance {
    private static Context mContext;
    private static Retrofit retrofit;
    private static String token;

    public RetrofitInstance(){

    }

    public static Retrofit getRetrofitInstanceall() {

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .build();
                return chain.proceed(newRequest);
            }})
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}