package com.example.irhabi_ecsboard.sendbird.updatepost;

/**
 * Created by programmer Jalanan on 27/08/2018
 */


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.irhabi_ecsboard.sendbird.model.User;
import com.example.irhabi_ecsboard.sendbird.service.RetrofitInstance;
import com.example.irhabi_ecsboard.sendbird.service.Router;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//class yang menangani update data user
public class UpdatePost extends AppCompatActivity{

    private User user;
    private Router router;
    private RetrofitInstance service;
    private Context mContext;
    private String strphoneType = "";

    public void postUpdate(final Context mContext, String imei, String token,
                           String lont, String lat, String time, String date){
        this.mContext = mContext;
        user = new User("","","",date,time,token,imei,
                lont,lat);
        service = new RetrofitInstance();
        router = service.getRetrofitInstanceall().create(Router.class);
        Call<User> call = router.Postregis(user,imei,"realupdate");
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                 if (response.body().getStatusrespon() == null){
                    Log.d("null", "null");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
