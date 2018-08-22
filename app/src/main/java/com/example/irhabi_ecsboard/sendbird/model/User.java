package com.example.irhabi_ecsboard.sendbird.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;
    @SerializedName("tanggal")
    String date;
    @SerializedName("time")
    String time;
    @SerializedName("token")
    String token;
    @SerializedName("imei")
    String imei;
    @SerializedName("lont")
    String lont;
    @SerializedName("lat")
    String lat;
    @SerializedName("status")
    String status;
    @SerializedName("Status")
    String Status;
    @SerializedName("usergrup")
    String ug;

    public User(String name, String username, String password, String tanggal, String time, String imei, String token, String lont, String lat){
        this.name = name;
        this.username = username;
        this.password = password;
        this.date = tanggal;
        this.time = time;
        this.token = token;
        this.imei = imei;
        this.lont = lont;
        this.lat = lat;
    }
    public int getId(){
        return id;
    }
    public String getUsergrup() {
        return ug;
    }

    public String getStatus(){
        return status;
    }

    public String getStatusrespon(){
        return Status;
    }

    public String setUsername(String username){
        this.username = username;
        return username;
    }
    public String getUsername(){
        return username;
    }

    public String setPassword(String password) {
        this.password = password;
        return password;
    }

    public String getName() {
        return name;
    }
}
