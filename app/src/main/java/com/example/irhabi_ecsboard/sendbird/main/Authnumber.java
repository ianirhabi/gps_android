package com.example.irhabi_ecsboard.sendbird.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irhabi_ecsboard.sendbird.R;
import com.example.irhabi_ecsboard.sendbird.fcm.Config;
import com.example.irhabi_ecsboard.sendbird.model.User;
import com.example.irhabi_ecsboard.sendbird.service.RetrofitInstance;
import com.example.irhabi_ecsboard.sendbird.service.Router;
import com.example.irhabi_ecsboard.sendbird.sesi.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class Authnumber extends AppCompatActivity {
    private String getnumber, getnama, getpassword;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    private static final String TAG = "PhoneAuthActivity";
    private Router router;
    @BindView(R.id.dikirm)
    TextView l;
    @BindView(R.id.satu)
    TextView sa;
    @BindView(R.id.dua)
    TextView du;
    @BindView(R.id.tiga)
    TextView ti;
    @BindView(R.id.empat)
    TextView em;
    @BindView(R.id.lima)
    TextView li;
    @BindView(R.id.enam)
    TextView en;
    @BindView(R.id.send)
    Button kirim;
    @BindView(R.id.sendlagi)
    Button kirimlagi;

    private SessionManager sesi;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String strphoneType = "";
    private User user;
    private RetrofitInstance retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authnumber);
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        getnumber = b.getString("parsing_number");
        l.setText("Nomor Anda " + getnumber);

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        startPhoneNumberVerification(getnumber);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Bundle b = getIntent().getExtras();
                            getnumber = b.getString("parsing_number");
                            getnama = b.getString("parsing_nama");
                            getpassword = b.getString("parsing_password");
                            Log.d(TAG, "signInWithCredential:success");

                            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {

                                    // checking for type intent filter
                                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                                        // gcm successfully registered
                                        // now subscribe to `global` topic to receive app wide notifications
                                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                                        Log.e("hai", "signInWithCredential:success");
                                        Readphone(getnama, getnumber, getpassword);

                                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                                        // new push notification is received

                                        String message = intent.getStringExtra("message");
                                        //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                                    }
                                }
                            };
                            Readphone(getnama,getnumber,getpassword);
                            FirebaseUser user = task.getResult().getUser();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Kode salah",Toast.LENGTH_LONG ).show();
                            }
                        }
                    }
                });
    }

    public void Readphone(String name, String username, String password) {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int phoneType = manager.getPhoneType();
        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                strphoneType = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                strphoneType = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                strphoneType = "NONE";
                break;
        }
        boolean isRoaming = manager.isNetworkRoaming();

        String PhoneType = strphoneType;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
            for (int i = 0; i < subscription.size(); i++) {
                SubscriptionInfo info = subscription.get(i);
                Log.d("debug", "number " + info.getIccId());
                Log.d("debug", "network name : " + info.getCarrierName());
                Log.d("debug", "country iso " + info.getCountryIso());
            }
        }
        String IMEInumber = manager.getDeviceId();
        String numberphone = manager.getLine1Number();
        Log.d("debug " ,"imeinya " + IMEInumber +" operator " + numberphone);
        displayFirebaseRegId(IMEInumber, name, username, password);
    }

    private void displayFirebaseRegId(String imei, String name, String username, String password) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String token = pref.getString("regId", null);
        Regis(name, username, password, imei, token);
        Log.e("debug", "Firebase reg id di auth number: " + token);
    }


    public void Regis(String name, String username, String password, String imei, String token){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date tanggalnya = new Date();
        String tanggal = dateFormat.format(tanggalnya);
        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
        Date jamnya = new Date();
        String jam = dateFormat2.format(jamnya);
        Log.d("masuk sini", "");
        user = new User(name, username, password, tanggal ,jam, token, imei, "1.2323", "106.77777");
        retrofit = new RetrofitInstance();
        Log.d("masuk sini", "");
        Router service = retrofit.getRetrofitInstanceall().create(Router.class);
        Call<User> call = service.Postregis(user, imei);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                Log.d("debug ", ""+ response.body().getStatusrespon());
                Toast.makeText(getApplicationContext(), "status " +response.body().getStatusrespon(), Toast.LENGTH_LONG).show();
                if (response.body().getStatusrespon().equals("berhasil insert")){
                    sesi = new SessionManager(getApplicationContext());
                    sesi.createRegisSession(getnama, getnumber, getpassword);
                    Intent i = null;
                    i = new Intent(Authnumber.this, Welcome.class);
                    Bundle getData = new Bundle();
                    getData.putString("parsingnumber", getnumber);
                    getData.putString("parsingnama", getnama);
                    getData.putString("parsingpass", getpassword);
                    i.putExtras(getData);
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal Mengambil Data Dari Server Pastikan Anda Terhubung Dengan Internet " , Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+62"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+62"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    @OnClick(R.id.send)
    void kirim(){
        try {
            String satu = sa.getText().toString();
            String dua = du.getText().toString();
            String tiga = ti.getText().toString();
            String empat = em.getText().toString();
            String lima = li.getText().toString();
            String enam = en.getText().toString();
            verifyPhoneNumberWithCode(mVerificationId, satu + dua + tiga + empat + lima + enam);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.sendlagi)
    void kirimlagi(){
        try {
        Bundle b = getIntent().getExtras();
        getnumber = b.getString("parsing_number");
        resendVerificationCode(getnumber, mResendToken);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(Authnumber.this, Welcome.class));
            finish();
        }
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
}
