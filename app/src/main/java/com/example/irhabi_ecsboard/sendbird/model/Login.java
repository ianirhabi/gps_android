package com.example.irhabi_ecsboard.sendbird.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("Status")
    private String status;
    @SerializedName("data")
    private User user;


    public Login(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }


}
