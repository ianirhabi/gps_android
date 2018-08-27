package com.example.irhabi_ecsboard.sendbird.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseIuran {
    @SerializedName("id")
    private int id;
    @SerializedName("status")
    private String status;
    @SerializedName("status_bayar")
    private String bayar;
    @SerializedName("status_belum_bayar")
    private int belumbayar;
    @SerializedName("data")
    private List<Iuran> data;

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getBayar() {
        return bayar;
    }

    public int getBelumbayar() {
        return belumbayar;
    }

    public List<Iuran> getData() {
        return data;
    }
}
