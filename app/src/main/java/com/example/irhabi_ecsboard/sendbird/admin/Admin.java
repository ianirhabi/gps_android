package com.example.irhabi_ecsboard.sendbird.admin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.Window;
import com.example.irhabi_ecsboard.sendbird.R;
import com.example.irhabi_ecsboard.sendbird.anggota.AnggotaActivity;
import com.example.irhabi_ecsboard.sendbird.main.MainActivity;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class Admin extends AppCompatActivity  {
    private  String arrayName []= {"Input Iuran Anggota", "Home"};
    private final long startTime = 1 * 1000;
    private final long interval = 1 * 1000;
    private boolean timerHasStarted = false;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        // set an exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }
        setContentView(R.layout.activity_admin);

        CircleMenu circleMenu = (CircleMenu)findViewById(R.id.menucircle);

        circleMenu.setMainMenu(Color.parseColor("#2F4F4F"),R.drawable.icon, R.drawable.ic_settings_white_24dp)
                .addSubMenu(Color.parseColor("#258cff"),R.drawable.anggota)
                .addSubMenu(Color.parseColor("#6d4c41"),R.drawable.home)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int i) {
                        if(arrayName[i].equals("Input Iuran Anggota")){
                            countDownTimer = new MyCountDownTimer(startTime, interval,arrayName[i]);
                            countDownTimer.start();
                            timerHasStarted = true;
                        }else if((arrayName[i].equals("Home"))){
                             countDownTimer = new MyCountDownTimer(startTime, interval,arrayName[i]);
                             countDownTimer.start();
                             timerHasStarted = true;
                        }
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
            if(b.equals("Home")) {
                Intent ii = new Intent(Admin.this, MainActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(ii, ActivityOptions.makeSceneTransitionAnimation(Admin.this).toBundle());
                }
            }
            if(b.equals("Input Iuran Anggota")){
                Intent ii = new Intent(Admin.this, AnggotaActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(ii, ActivityOptions.makeSceneTransitionAnimation(Admin.this).toBundle());
                }
            }
        }
        @Override
        public void onTick(long millisUntilFinished) {

        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
