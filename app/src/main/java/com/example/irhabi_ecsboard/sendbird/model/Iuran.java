package com.example.irhabi_ecsboard.sendbird.model;

import com.google.gson.annotations.SerializedName;

public class Iuran {
    @SerializedName("id")
    int id;
    @SerializedName("userid")
    int userid;
    @SerializedName("status_minggu")
    String minggu;
    @SerializedName("status_bulan")
    String bulan;
    @SerializedName("status_tahun")
    String tahun;
    @SerializedName("status_bayar")
    String bayar;
    @SerializedName("nama_anggota")
    String anggota;
    @SerializedName("tanggal_bayar")
    String tanggal;
    @SerializedName("status")
    String status;

    public Iuran(int id, String minggu, String bulan, String tahun, String bayar, String anggota, String tanggal) {
        this.userid = id;
        this.minggu = minggu;
        this.bulan = bulan;
        this.tahun = tahun;
        this.bayar = bayar;
        this.anggota = anggota;
        this.tanggal = tanggal;
    }

    public int getUserid() {
        return userid;
    }

    public String getMinggu() {
        return minggu;
    }

    public String getBulan() {
        return bulan;
    }

    public String getTahun() {
        return tahun;
    }

    public String getBayar() {
        return bayar;
    }

    public String getAnggota() {
        return anggota;
    }

    public String getTanggal() {
        return tanggal;
    }


    public String getStatus(){
        return status;
    }
}
