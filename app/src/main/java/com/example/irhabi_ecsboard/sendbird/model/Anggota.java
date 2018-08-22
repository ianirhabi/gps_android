package com.example.irhabi_ecsboard.sendbird.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Anggota {
    @SerializedName("data")
    private ArrayList<User> data;
    @SerializedName("Status")
    private String status;

    public ArrayList<User> getData(){
        return data;
    }
    public String getStatus(){
        return status;
    }
}
