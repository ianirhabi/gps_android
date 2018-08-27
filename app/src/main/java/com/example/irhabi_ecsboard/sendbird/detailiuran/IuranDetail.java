package com.example.irhabi_ecsboard.sendbird.detailiuran;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irhabi_ecsboard.sendbird.R;
import com.example.irhabi_ecsboard.sendbird.model.Iuran;
import com.example.irhabi_ecsboard.sendbird.model.ResponseIuran;
import com.example.irhabi_ecsboard.sendbird.service.RetrofitInstance;
import com.example.irhabi_ecsboard.sendbird.service.Router;
import com.example.irhabi_ecsboard.sendbird.utils.PreferenceUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IuranDetail extends AppCompatActivity {

    //updatebayar
    private Button up_bsatu, up_bdua,up_btiga,up_bempat,
                    up_bayarsatu, up_bayardua,up_bayartiga,up_bayarempat;

    private TextView name, num, satu, satutujuh, duwa, mduwa,
            tga,ga, pat, ppat, pembayaran_status_m1,pembayaran_status_m2,
            pembayaran_status_m3, pembayaran_status_m4;

    private TextView na, nu, sat, tuj, wa,dwa, gaa,mga, empt, mpatt,
            m1_pembayaran_status, m2_pembayaran_status, m3_pembayaran_status, m4_pembayaran_status;

    //belums
    private TextView blsatu, bldua, bltiga, blempat, blssatu, blsdua,blstiga,blsempat;

    private RelativeLayout a;
    private RelativeLayout aa ;
    private Spinner stt, ts, tahun, thn ;
    private LinearLayout msatu , msat, mdua, dua, mtga, tiga, mpat, empat;
    private Router router;
    private RetrofitInstance service;
    private ImageView blik, balik;

    private LinearLayout click, klik;

    // untuk waktu
    private final long startTime = 1 * 500;
    private final long interval = 1 * 1000;
    private boolean timerHasStarted = false;
    private CountDownTimer countDownTimer;
    private Iuran iuran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iuran_detail);
        Widget();
        showData();
    }

    public void Widget(){

       name = (TextView)findViewById(R.id.tv_name);
       num = (TextView)findViewById(R.id.tv_number);
       satu = (TextView)findViewById(R.id.kesatu);

       duwa = (TextView)findViewById(R.id.kedua);
       mduwa = (TextView)findViewById(R.id.keduatanggal);

       tga = (TextView)findViewById(R.id.ketiga);
       ga = (TextView)findViewById(R.id.ketigatanggal);

       pat = (TextView)findViewById(R.id.keempat);
       ppat = (TextView)findViewById(R.id.keempattanggal);

       satutujuh = (TextView)findViewById(R.id.satutujuh);

       ts = (Spinner)findViewById(R.id.bulan);
       a = (RelativeLayout)findViewById(R.id.bulanback);
       msat = (LinearLayout)findViewById(R.id.msatu);
       mdua = (LinearLayout)findViewById(R.id.mdua);
       mtga = (LinearLayout)findViewById(R.id.mtiga);
       mpat = (LinearLayout)findViewById(R.id.mempat);
       click =(LinearLayout)findViewById(R.id.update);
       this.klik = click;
       thn = (Spinner)findViewById(R.id.tahun);
       this.tahun = thn;


       blssatu = (TextView)findViewById(R.id.belums_msatu);
       this.blsatu = blssatu;
       blsdua = (TextView)findViewById(R.id.belums_mdua);
       this.bldua = blsdua;
       blstiga = (TextView)findViewById(R.id.belums_mtiga);
       this.bltiga = blstiga;
       blsempat = (TextView)findViewById(R.id.belums_mempat);
       this.blempat = blsempat;

       //status pembayaran
       pembayaran_status_m1 = (TextView)findViewById(R.id.pembayaran_msatu);
       this.m1_pembayaran_status = pembayaran_status_m1;

       pembayaran_status_m2 = (TextView)findViewById(R.id.pembayaran_mdua);
       this.m2_pembayaran_status = pembayaran_status_m2;

       pembayaran_status_m3 = (TextView)findViewById(R.id.pembayaran_mtiga);
       this.m3_pembayaran_status = pembayaran_status_m3;

       pembayaran_status_m4 = (TextView)findViewById(R.id.pembayaran_mempat);
       this.m4_pembayaran_status = pembayaran_status_m4;

       // Linear and text and button
       this.dua = mdua;  this.wa = duwa; this.mga = ga;
       this.tiga = mtga;  this.dwa = mduwa; this.empt = pat;
       this.empat = mpat; this.gaa = tga; this.mpatt = ppat;
       this.msatu = msat;
       this.stt = ts;
       this.aa = a;
       this.na = name;
       this.nu = num;
       this.sat= satu;
       this.tuj = satutujuh;

       //update bayar
       up_bayarsatu = (Button)findViewById(R.id.update_bayarsatu);
       this.up_bsatu = up_bayarsatu;

       up_bayardua = (Button)findViewById(R.id.update_bayardua);
       this.up_bdua = up_bayardua;

       up_bayartiga = (Button)findViewById(R.id.update_bayartiga);
       this.up_btiga = up_bayartiga;

       up_bayarempat = (Button)findViewById(R.id.update_bayarempat);
       this.up_bempat = up_bayarempat;
    }

    public void showData(){
        String [] st = {"Jan", "Feb", "Mar", "Apr", "Mei",
                "Jun", "Jul", "Agust", "Sept", "Okt", "Nov", "Des"};
        ArrayAdapter adapterBulan = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, st);
        stt.setAdapter(adapterBulan);

        String [] th = {"2018","2019","2020", "2021", "2022", "2023"
                    ,"2024","2025", "2026", "2027", "2028", "2030"};

        ArrayAdapter adapterTahun = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,th);
        tahun.setAdapter(adapterTahun);

        Bundle b = getIntent().getExtras();
        final int getId = b.getInt("id");
        final String anggota = b.getString("anggota");
        final String number = b.getString("nomor");
        listener();
        na.setText(anggota);
        nu.setText(number);
    }

    public void listener(){
        aa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Bulan " + stt.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        msatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.pindah_kesamping_kanan);
                msat.startAnimation(animation);
                String status = sat.getText().toString();
                countDownTimer = new MyCountDownTimer(startTime, interval, status);
                countDownTimer.start();
                timerHasStarted = true;
            }
        });

        dua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.pindah_kesamping_kanan);
                dua.startAnimation(animation);
                String status = wa.getText().toString();
                countDownTimer = new MyCountDownTimer(startTime, interval, status);
                countDownTimer.start();
                timerHasStarted = true;
            }
        });

        tiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.pindah_kesamping_kanan);
                tiga.startAnimation(animation);
                String status = gaa.getText().toString();
                countDownTimer = new MyCountDownTimer(startTime, interval, status);
                countDownTimer.start();
                timerHasStarted = true;
            }
        });

        empat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.pindah_kesamping_kanan);
                empat.startAnimation(animation);
                String status = empt.getText().toString();
                countDownTimer = new MyCountDownTimer(startTime, interval, status);
                countDownTimer.start();
                timerHasStarted = true;
            }
        });

        klik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                satu.setVisibility(View.VISIBLE);
                tuj.setVisibility(View.VISIBLE);
                tuj.setText("Tanggal 1-Tanggal 7");
                m1_pembayaran_status.setVisibility(View.GONE);
                blsatu.setVisibility(View.GONE);

                wa.setVisibility(View.VISIBLE);
                dwa.setVisibility(View.VISIBLE);
                dwa.setText("Tanggal 8-Tanggal 15");
                m2_pembayaran_status.setVisibility(View.GONE);
                bldua.setVisibility(View.GONE);

                gaa.setVisibility(View.VISIBLE);
                mga.setVisibility(View.VISIBLE);
                mga.setText("Tanggal 16-Tanggal 22");
                m3_pembayaran_status.setVisibility(View.GONE);
                bltiga.setVisibility(View.GONE);

                empt.setVisibility(View.VISIBLE);
                mpatt.setVisibility(View.VISIBLE);
                mpatt.setText("Tanggal 23-Tanggal 29");
                m4_pembayaran_status.setVisibility(View.GONE);
                blempat.setVisibility(View.GONE);

            }
        });
    }

    class MyCountDownTimer extends CountDownTimer {

        private String b;
        public MyCountDownTimer(long startTime, long interval, String a) {
            super(startTime, interval);
            this.b = a;

        }

        @Override
        public void onFinish() {

            if(b.equals("Minggu ke -1")){
                satu.setVisibility(View.GONE);
                tuj.setVisibility(View.GONE);
                getIuran("1",m1_pembayaran_status,blsatu,tuj,up_bsatu,"6");
            }else if(b.equals("Minggu ke -2")) {
                wa.setVisibility(View.GONE);
                dwa.setVisibility(View.GONE);
                getIuran("2",m2_pembayaran_status,bldua,dwa,up_bdua,"13");
            }else if(b.equals("Minggu ke -3")){
                gaa.setVisibility(View.GONE);
                mga.setVisibility(View.GONE);
                getIuran("3",m3_pembayaran_status,bltiga,mga,up_btiga,"20");
            }else if(b.equals("Minggu ke -4")){
                empt.setVisibility(View.GONE);
                mpatt.setVisibility(View.GONE);
                getIuran("4",m4_pembayaran_status,blempat,mpatt,up_bempat,"28");
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

    public void getIuran(final String minggu , final TextView st, final TextView BLs,
                         final TextView tanggal, final Button updatebayar, final String tglmurni){

        service = new RetrofitInstance();
        router = service.getRetrofitInstanceall().create(Router.class);
        Bundle b = getIntent().getExtras();
        final int getId = b.getInt("id");
        Log.d("debug ", "id " + getId);
        final String bulan = stt.getSelectedItem().toString();
        final String th = tahun.getSelectedItem().toString();
        Call<ResponseIuran> call = router.Getiuran(bulan,th,getId,minggu);
        call.enqueue(new Callback<ResponseIuran>() {

            @Override
            public void onResponse(Call<ResponseIuran> call, Response<ResponseIuran> response) {
                if (response.body().getStatus().equals("berhasil")){
                    Toast.makeText(getApplicationContext(),"status "
                            + response.body().getStatus(),Toast.LENGTH_SHORT).show();
                    st.setVisibility(View.VISIBLE);
                    if (response.body().getData() == null){
                        Toast.makeText(getApplicationContext(),"data null ", Toast.LENGTH_SHORT).show();
                    }else {
                        List<Iuran>data = response.body().getData();
                        tanggal.setVisibility(View.VISIBLE);
                        BLs.setVisibility(View.GONE);
                        updatebayar.setVisibility(View.GONE);
                        tanggal.setText("Tanggal pembayaran iuran : " + data.get(0).getTanggal());
                        st.setText(data.get(0).getBayar());
                    }

                }else if (response.body().getBelumbayar() == 0){
                    BLs.setVisibility(View.VISIBLE);
                    st.setVisibility(View.GONE);
                    BLs.setText(response.body().getBayar());
                    String status = PreferenceUtils.getUserGrup(IuranDetail.this);
                    if(status.equals("1") || status.equals("2")||status.equals("3")){
                        updatebayar.setVisibility(View.VISIBLE);
                        updateBayar(updatebayar, minggu, bulan,th, tglmurni);
                    }
                }else if (response.body().getStatus() == null){
                    Toast.makeText(getApplicationContext(),"Status null", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Tidak bisa konek ke server mungkin kuota anda telah habis", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseIuran> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Error connect to server gps", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateBayar (final Button b, final String minggu, final String bulan,
                             final String tahun, final String tanggal){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postupdateBayar(b,minggu, bulan, tahun, tanggal);
            }
        });
    }

    public void postupdateBayar(final Button bls,String minggu, String bulan, String tahun, String tanggal){

        Bundle b = getIntent().getExtras();
        final int getId = b.getInt("id");
        final String anggota = b.getString("anggota");
        final String number = b.getString("nomor");
        String date = tahun + "-" + bulan + "-"+tanggal;
        iuran = new Iuran(getId,minggu,bulan,tahun,"selesai",anggota,date);

        service = new RetrofitInstance();

        router = service.getRetrofitInstanceall().create(Router.class);

        Call<Iuran> call = router.PostiIuran(iuran);

        call.enqueue(new Callback<Iuran>() {
            @Override
            public void onResponse(Call<Iuran> call, Response<Iuran> response) {
                if(response.body().getStatus()==null){
                    Toast.makeText(getApplicationContext(),"data null", Toast.LENGTH_SHORT).show();
                }else if(response.body().getStatus().equals("berhasil")){
                    Iuranpostrefresh(bls);
                    Toast.makeText(getApplicationContext(),response.body().getStatus()+" update data", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"something wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Iuran> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Gagal connect ke server gps comunity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Iuranpostrefresh(Button b){
        b.setVisibility(View.GONE);
        satu.setVisibility(View.VISIBLE);
        tuj.setVisibility(View.VISIBLE);
        tuj.setText("Tanggal 1-Tanggal 7");
        m1_pembayaran_status.setVisibility(View.GONE);
        blsatu.setVisibility(View.GONE);

        wa.setVisibility(View.VISIBLE);
        dwa.setVisibility(View.VISIBLE);
        dwa.setText("Tanggal 8-Tanggal 15");
        m2_pembayaran_status.setVisibility(View.GONE);
        bldua.setVisibility(View.GONE);

        gaa.setVisibility(View.VISIBLE);
        mga.setVisibility(View.VISIBLE);
        mga.setText("Tanggal 16-Tanggal 22");
        m3_pembayaran_status.setVisibility(View.GONE);
        bltiga.setVisibility(View.GONE);

        empt.setVisibility(View.VISIBLE);
        mpatt.setVisibility(View.VISIBLE);
        mpatt.setText("Tanggal 23-Tanggal 29");
        m4_pembayaran_status.setVisibility(View.GONE);
        blempat.setVisibility(View.GONE);
    }
}
