package com.example.irhabi_ecsboard.sendbird.main;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irhabi_ecsboard.sendbird.admin.Admin;
import com.example.irhabi_ecsboard.sendbird.fcm.Config;
import com.example.irhabi_ecsboard.sendbird.model.Login;
import com.example.irhabi_ecsboard.sendbird.service.RetrofitInstance;
import com.example.irhabi_ecsboard.sendbird.service.Router;
import com.example.irhabi_ecsboard.sendbird.sesi.SessionManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.example.irhabi_ecsboard.sendbird.R;
import com.example.irhabi_ecsboard.sendbird.utils.PreferenceUtils;
import com.example.irhabi_ecsboard.sendbird.utils.PushUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private CoordinatorLayout mLoginLayout;
    private TextInputEditText mUserIdConnectEditText, mUserNicknameEditText;
    private Button mConnectButton;
    private ContentLoadingProgressBar mProgressBar;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PERMISSION_READ_STATE = 123;
    private static final int PERMISSION_REQUEST_READ_CONTACTS = 100;
    private SessionManager sesi;
    private Router router;
    private RetrofitInstance service;
    private Login user;
    private final long startTime = 5 * 1000;
    private final long interval = 1 * 1000;
    private boolean timerHasStarted = false;
    private CountDownTimer countDownTimer;

    @BindView(R.id.regis)
    TextView f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        // set an exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        PreferenceUtils.setStatusLogin(LoginActivity.this, "tutup");
        // inside your activity (if you did not enable transitions in your theme)

        mLoginLayout = (CoordinatorLayout) findViewById(R.id.layout_login);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };

        displayFirebaseRegId();

        mUserIdConnectEditText = (TextInputEditText) findViewById(R.id.edittext_login_user_id);
        mUserNicknameEditText = (TextInputEditText) findViewById(R.id.edittext_login_user_nickname);

        mUserIdConnectEditText.setText(PreferenceUtils.getUserId(this));
        mUserNicknameEditText.setText(PreferenceUtils.getNickname(this));

        mConnectButton = (Button) findViewById(R.id.button_login_connect);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mUserIdConnectEditText.getText().toString();
                // Remove all spaces from userID
                userId = userId.replaceAll("\\s", "");
                String userNickname = mUserNicknameEditText.getText().toString();
                showProgressBar(true);
                Postlogin(userId, userNickname);
            }
        });

        mUserIdConnectEditText.setSelectAllOnFocus(true);
        mUserNicknameEditText.setSelectAllOnFocus(true);

        // A loading indicator
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar_login);

        // Display current SendBird and app versions in a TextView
        String sdkVersion = String.format(getResources().getString(R.string.all_app_version),
                BaseApplication.VERSION, SendBird.getSDKVersion());
//        ((TextView) findViewById(R.id.text_login_versions)).setText(sdkVersion);

        try {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        PERMISSION_REQUEST_READ_CONTACTS); }

        }catch (Exception e){

        }

        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck2 == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_READ_STATE);
        }

    }

    @OnClick(R.id.regis)
    public void regis(){
        Intent i = new Intent(LoginActivity.this, Register.class);
        startActivity(i);
        finish();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("debug", "Firebase reg id LOGIN: " + regId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(PreferenceUtils.getConnected(this)) {
            showProgressBar(true);
            Toast.makeText(getApplicationContext(),"Login otomatis silahkan tunggu", Toast.LENGTH_SHORT).show();
            Postlogin(PreferenceUtils.getUserId(this),PreferenceUtils.getPassword(this));
        }
    }

    /**
     * Attempts to connect a user to SendBird.
     * @param userId    The unique ID of the user.
     * @param userNickname  The user's nickname, which will be displayed in chats.
     */
    private void connectToSendBird(final String userId, final String userNickname, final  String usergrup) {
        // Show the loading indicator
        showProgressBar(true);
        mConnectButton.setEnabled(false);

        ConnectionManager.login(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Callback received; hide the progress bar
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            LoginActivity.this, "" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show login failure snackbar
                    showSnackbar("Login ke gps gagal");
                    showProgressBar(false);
                    mConnectButton.setEnabled(true);
                    PreferenceUtils.setConnected(LoginActivity.this, false);
                    return;
                }

                PreferenceUtils.setNickname(LoginActivity.this, user.getNickname());
                PreferenceUtils.setProfileUrl(LoginActivity.this, user.getProfileUrl());
                PreferenceUtils.setConnected(LoginActivity.this, true);

                // Update the user's nickname
                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();

                if(usergrup.equals("1") || usergrup.equals("2") || usergrup.equals("3")){
                    countDownTimer = new MyCountDownTimer(startTime, interval, usergrup);
                    countDownTimer.start();
                    timerHasStarted = true;
                }else {
                    countDownTimer = new MyCountDownTimer(startTime, interval, usergrup);
                    countDownTimer.start();
                    timerHasStarted = true;
                    // Proceed to MainActivity
                }
            }
        });
    }

    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(LoginActivity.this, null);
    }

    /**
     * Updates the user's nickname.
     * @param userNickname  The new nickname of the user.
     */
    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            LoginActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();
                    // Show update failed snackbar
                    showSnackbar("Update user nickname failed");
                    return;
                }

                PreferenceUtils.setNickname(LoginActivity.this, userNickname);
            }
        });
    }

    // Displays a Snackbar from the bottom of the screen
    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(mLoginLayout, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    // Shows or hides the ProgressBar
    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            moveTaskToBack(true);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResult) {
        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(getApplicationContext(), "Anda Harus Memberi Izin Aplikasi Untuk Melanjutkan", Toast.LENGTH_SHORT).show();
                System.exit(0);
                finish();
            }
        }

        if (requestCode == PERMISSION_READ_STATE) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"Silahkan buka kembali aplikasi gps", Toast.LENGTH_SHORT).show();
               finish();
            } else {
                Toast.makeText(getApplicationContext(), "Anda Harus Memberi Izin Aplikasi Untuk Melanjutkan", Toast.LENGTH_SHORT).show();
                System.exit(0);
                finish();
            }
        }
    }

    public void Postlogin(String username, final String password){

       user = new Login(username,password);
       service = new RetrofitInstance();
       router = service.getRetrofitInstanceall().create(Router.class);
       Call<Login> call = router.Postlogin(user);
       call.enqueue(new Callback<Login>() {
           @Override
           public void onResponse(Call<com.example.irhabi_ecsboard.sendbird.model.Login> call, Response<com.example.irhabi_ecsboard.sendbird.model.Login> response) {
               if(response.body().getStatus().equals("berhasil")){
                   if(response.body().getUser().getStatus().equals("pending")){
                       showProgressBar(false);
                       Toast.makeText(getApplicationContext(),"Status akun anda belum aktif silahkan menghubungi Admin ",Toast.LENGTH_SHORT).show();
                   }else {

                       if (response.body().getUser().getUsergrup().equals("4")) {
                           String userId, userNickname;
                           sesi = new SessionManager(getApplicationContext());
                           HashMap<String, String> usersesi = sesi.getUserDetails();
                           userId = usersesi.get(SessionManager.KEY_USERNAME);
                           userNickname = usersesi.get(SessionManager.KEY_NAME);

                           PreferenceUtils.setUserGrup(LoginActivity.this, response.body().getUser().getUsergrup());
                           PreferenceUtils.setUserId(LoginActivity.this, response.body().getUser().getUsername());
                           PreferenceUtils.setPassword(LoginActivity.this, password);
                           PreferenceUtils.setNickname(LoginActivity.this, response.body().getUser().getName());
                           connectToSendBird(response.body().getUser().getUsername(), response.body().getUser().getName(), response.body().getUser().getUsergrup());

                       } else if (response.body().getUser().getUsergrup().equals("1") || response.body().getUser().getUsergrup().equals("2") ||
                               response.body().getUser().getUsergrup().equals("3")) {

                           String userId, userNickname;
                           sesi = new SessionManager(getApplicationContext());
                           HashMap<String, String> usersesi = sesi.getUserDetails();
                           userId = usersesi.get(SessionManager.KEY_USERNAME);
                           userNickname = usersesi.get(SessionManager.KEY_NAME);
                           PreferenceUtils.setUserGrup(LoginActivity.this, response.body().getUser().getUsergrup());
                           PreferenceUtils.setUserId(LoginActivity.this, response.body().getUser().getUsername());
                           PreferenceUtils.setNickname(LoginActivity.this, response.body().getUser().getName());
                           PreferenceUtils.setPassword(LoginActivity.this, password);
                           connectToSendBird(response.body().getUser().getUsername(), response.body().getUser().getName(), response.body().getUser().getUsergrup());

                       } else {
                           showProgressBar(false);
                           Toast.makeText(getApplicationContext(), "Anda Tidak terdaftar", Toast.LENGTH_LONG).show();
                       }
                   }

               }else if(response.body().getStatus() == null){
                   showProgressBar(false);
                   Toast.makeText(getApplicationContext(),"Statusnya null", Toast.LENGTH_LONG).show();
               }
               //KETIKA SALAH PASSWORD
               else {
                   showProgressBar(false);
                   Toast.makeText(getApplicationContext(),"Something wrong", Toast.LENGTH_LONG).show();
               }
           }

           @Override
           public void onFailure(Call<com.example.irhabi_ecsboard.sendbird.model.Login> call, Throwable t) {
               showProgressBar(false);
               Toast.makeText(getApplicationContext(),"gagal connect ke server", Toast.LENGTH_LONG).show();
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
            if(b.equals("1")||b.equals("2")||b.equals("3")) {
                showProgressBar(false);
                Intent intent = new Intent(LoginActivity.this, Admin.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                }
            }else {
                showProgressBar(false);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                }
            }
        }
        @Override
        public void onTick(long millisUntilFinished) {

        }
    }
}
