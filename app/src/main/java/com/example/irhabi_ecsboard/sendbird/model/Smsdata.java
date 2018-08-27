package com.example.irhabi_ecsboard.sendbird.model;

/**
 * Created by Programmer Jalanan on 12/08/2018.
 */

import com.google.gson.annotations.SerializedName;

public class Smsdata {
    @SerializedName("number")
    String number;
    @SerializedName("message")
    String message;
    @SerializedName("status")
    String status;

    public Smsdata(String number, String  message){
        this.number = number;
        this.message = message;
    }
    public String getStatus(){
        return status;
    }
}
