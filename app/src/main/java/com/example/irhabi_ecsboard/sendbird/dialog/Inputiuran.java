package com.example.irhabi_ecsboard.sendbird.dialog;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irhabi_ecsboard.sendbird.R;
import com.example.irhabi_ecsboard.sendbird.anggota.AnggotaActivity;
import com.example.irhabi_ecsboard.sendbird.detailiuran.IuranDetail;
import com.example.irhabi_ecsboard.sendbird.model.Iuran;
import com.example.irhabi_ecsboard.sendbird.model.User;
import com.example.irhabi_ecsboard.sendbird.service.RetrofitInstance;
import com.example.irhabi_ecsboard.sendbird.service.Router;
import com.example.irhabi_ecsboard.sendbird.sesi.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class Inputiuran {

    private Router router;
    private RetrofitInstance service;
    private Iuran ir;
    Context mContext;
    private SessionManager sesi;
    private String number;
    private Dialog dlg;
    private User user;
    private String imei;

    public void showInput(Activity activity, String msg, final Context mContext,
                          final int id,final String anggota, ArrayAdapter adapter, String number, String imei){
        this.mContext = mContext;
        this.number = number;
        this.imei =imei;
        dialogView(activity, mContext, id, anggota,  adapter);
    }

    public void dialogView(Activity activity, Context mContext, int id, String anggota, ArrayAdapter adapter){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_inputiuran);
        this.dlg = dialog;
        widget(dialog, mContext, id, anggota,  adapter);
        dialog.show();
    }

    public void widget(final Dialog dialog, final Context mContext,
                       final int id, final String anggota, final ArrayAdapter adapter){
        final Button batal = (Button)dialog.findViewById(R.id.batal);
        final Button input = (Button) dialog.findViewById(R.id.input);
        final Button update = (Button) dialog.findViewById(R.id.update);
        final Button detail = (Button) dialog.findViewById(R.id.detail);
        final EditText tanggal = (EditText) dialog.findViewById(R.id.minggu);
        final Spinner stbayar = (Spinner) dialog.findViewById(R.id.stbayar);
        final TextView ts  = (TextView) dialog.findViewById(R.id.inputst);
        st(ts, anggota);
        checkSesion(input,batal,update,detail, stbayar, ts, adapter,id, anggota, dialog);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void postIuran(final int id, String statusminggu, String statusbulan,
                          String statustahun,String anggota, String statusbayar){

        DateFormat Tanggal = new SimpleDateFormat("yyyy-MM-dd");
        Date tngl = new Date();

        ir = new Iuran(id,statusminggu,statusbulan,statustahun,
                statusbayar, anggota, Tanggal.format(tngl));

        service = new RetrofitInstance();

        router = service.getRetrofitInstanceall().create(Router.class);

        Call<Iuran> call = router.PostiIuran(ir);
        call.enqueue(new Callback<Iuran>() {
            @Override
            public void onResponse(Call<Iuran> call, Response<Iuran> response) {
                if(response.body().getStatus() == null){
                    Toast.makeText(mContext,"Status  null", Toast.LENGTH_LONG ).show();
                    dlg.dismiss();
                } else if(response.body().getStatus().equals("berhasil")){
                    Toast.makeText(mContext,"Berhasil Memasukan data", Toast.LENGTH_LONG ).show();
                    dlg.dismiss();
                }else if(response.body().getStatus().equals("sudahinputbaru")){
                    Toast.makeText(mContext,"data tidak bisa di input karena anda sudah memasukan input iuran terbaru pada anggota ini" +
                            " masukan iuran input terbaru di minggu selanjutnya ..... terima kasih ^_^", Toast.LENGTH_LONG ).show();
                    dlg.dismiss();
                }
                else {
                    Toast.makeText(mContext,"", Toast.LENGTH_LONG ).show();
                    dlg.dismiss();
                }
            }
            @Override
            public void onFailure(Call<Iuran> call, Throwable t) {
                Toast.makeText(mContext,"gagal konek ke server", Toast.LENGTH_LONG ).show();
            }
        });
    }

    public void st(TextView f, String anggota){
        DateFormat Tanggal = new SimpleDateFormat("dd");
        Date tngl = new Date();
        DateFormat Bulan = new SimpleDateFormat("MM");
        Date bulan = new Date();
        DateFormat Tahun = new SimpleDateFormat("yyyy");
        Date thn = new Date();
        String user = anggota.replace("a","<font color='#c5c5c5'>a</font>");
        String yy = Tanggal.format(tngl);
        int tgl = Integer.parseInt(yy);

        if (tgl <= 7){
            f.setText("Anda akan menginput iuran minngu ke 1 " + "bulan " + Bulan.format(bulan) + " "
                    + Tahun.format(thn) + " pada anggota bernama "+ Html.fromHtml(user) + " dengan status pembayaran iuran ");
        }else if (tgl > 7 && tgl <= 15){
            f.setText("Anda akan menginput iuran minggu ke 2 " + "bulan " + Bulan.format(bulan) + " "
                    + Tahun.format(thn) + " pada anggota bernama "+ Html.fromHtml(user)+ " dengan status pembayaran iuran ");
        }else if (tgl > 15 && tgl <= 22){
            f.setText("Anda akan menginput iuran minggu ke 3 " + "bulan " + Bulan.format(bulan) + " "
                    + Tahun.format(thn) + " pada anggota bernama "+ Html.fromHtml(user) + " dengan status pembayaran iuran ");
        }else if (tgl > 22 && tgl <= 29){
            f.setText("Anda akan menginput iuran minggu ke 4 " + "bulan " + Bulan.format(bulan) + " "
                    + Tahun.format(thn) + " pada anggota bernama "+ Html.fromHtml(user) + " dengan status pembayaran iuran ");
        }
    }

    public void showButton(Button input, Button batal, Button update,
                           Button detail){
        input.setVisibility(View.VISIBLE);
        batal.setVisibility(View.VISIBLE);
        update.setVisibility(View.VISIBLE);
        detail.setVisibility(View.VISIBLE);
    }

    public void hideButton(Button input, Button batal, Button update,
                           Button detail){
        input.setVisibility(View.GONE);
        batal.setVisibility(View.GONE);
        update.setVisibility(View.GONE);
        detail.setVisibility(View.GONE);
    }

    public  void showSpiner(Spinner a, TextView b){
        a.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);
    }

    public void checkSesion(final Button input, final Button batal, final Button update,
                            final Button detail, final Spinner st, final TextView lihat,
                            final ArrayAdapter adapter, final int id,
                            final String anggota, final  Dialog dialog){

        sesi = new SessionManager(mContext);
        HashMap<String, String> usersesi = sesi.getUserDetails();
        String status = usersesi.get(SessionManager.KEY_STATUS);

        if (status.equals("1")){

            showButton(input, batal,update,detail);

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, IuranDetail.class);
                    Bundle ambildata = new Bundle();
                    ambildata.putInt("id", id);
                    ambildata.putString("anggota", anggota);
                    ambildata.putString("nomor", number);
                    i.putExtras(ambildata);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mContext.startActivity(i, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                    }
                    dlg.dismiss();
                }
            });

            input.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    showSpiner(st, lihat);
                    hideButton(input,batal,update,detail);
                    input.setVisibility(View.VISIBLE);
                    st.setAdapter(adapter);
                    final DateFormat Tanggal = new SimpleDateFormat("dd");
                    final Date tngl = new Date();
                    final DateFormat Bulan = new SimpleDateFormat("MM");
                    final Date bulan = new Date();
                    final DateFormat Tahun = new SimpleDateFormat("yyyy");
                    final Date thn = new Date();
                    batal.setVisibility(View.VISIBLE);

                    input.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Toast.makeText(mContext,Bulan.format(bulan),Toast.LENGTH_SHORT);
                            Toast.makeText(mContext,Tahun.format(thn),Toast.LENGTH_SHORT);

                            String tanggalMasukan = Tanggal.format(tngl);
                            String status_minggu, statusbayar;
                            int tgl = Integer.parseInt(tanggalMasukan);
                            if (tgl <= 7){
                                status_minggu = "1";
                                statusbayar = st.getSelectedItem().toString();
                                postIuran(id, status_minggu, Bulan.format(bulan),Tahun.format(thn), anggota,statusbayar);
                            }else if (tgl > 7 && tgl <= 15){
                                status_minggu = "2";
                                statusbayar = st.getSelectedItem().toString();
                                postIuran(id, status_minggu, Bulan.format(bulan),Tahun.format(thn),anggota,statusbayar);
                            }else if (tgl > 15 && tgl <= 22){
                                status_minggu = "3";
                                statusbayar = st.getSelectedItem().toString();
                                postIuran(id, status_minggu, Bulan.format(bulan),Tahun.format(thn),anggota,statusbayar);
                            }else if (tgl > 22 && tgl <= 29){
                                status_minggu = "4";
                                statusbayar = st.getSelectedItem().toString();
                                postIuran(id, status_minggu, Bulan.format(bulan),Tahun.format(thn),anggota,statusbayar);
                            }
                        }
                    });
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideButton(input,batal,update,detail);
                    TextView tidakya = (TextView)dialog.findViewById(R.id.yatidak);
                    Button ya = (Button) dialog.findViewById(R.id.ya);
                    Button tidak = (Button)dialog.findViewById(R.id.tidak);
                    tidakya.setVisibility(View.VISIBLE);
                    ya.setVisibility(View.VISIBLE);
                    tidak.setVisibility(View.VISIBLE);
                    tidakya.setText("Apakah Anda yakin ingin mengaktifkan akun dari anggota bernama "+ anggota);
                    tidak.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           dlg.dismiss();
                       }
                    });
                    ya.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                            postUpdate();
                       }
                    });
                }
            });
        }
    }

    void postUpdate(){
        user = new User("","","","","","",imei,
                "","");
        service = new RetrofitInstance();

        router = service.getRetrofitInstanceall().create(Router.class);

        Call<User> call = router.Postregis(user,imei,"aktif");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getStatusrespon().equals("berhasil")){
                    Intent i = new Intent(mContext, AnggotaActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mContext.startActivity(i, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                    }
                    dlg.dismiss();
                    Toast.makeText(mContext,"Akun berhasil di aktifkan", Toast.LENGTH_SHORT).show();
                }else if (response.body().getStatusrespon() == null){
                    Toast.makeText(mContext,"Data null", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext,"Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(mContext,"GAGAL connect ke server GPS comunity", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
