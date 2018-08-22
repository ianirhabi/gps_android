package com.example.irhabi_ecsboard.sendbird.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irhabi_ecsboard.sendbird.R;
import com.example.irhabi_ecsboard.sendbird.sesi.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Welcome extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @BindView(R.id.keluar)
    Button logout;

    @BindView(R.id.nam)
    TextView nama;

    @BindView(R.id.user)
    TextView usernam;

    @BindView(R.id.passs)
    TextView pasword;

    @BindView(R.id.image_logo)
    ImageView logo;
    private SessionManager sesi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth!=null){
            try {
                    sesi = new SessionManager(getApplicationContext());
                    HashMap<String, String> usersesi = sesi.getUserDetails();
                    String name = usersesi.get(SessionManager.KEY_NAME);
                    String username = usersesi.get(SessionManager.KEY_USERNAME);
                    String password = usersesi.get(SessionManager.KEY_PASSWORD);
                    nama.setText("nama : "+name); usernam.setText("username :" + username);pasword.setText("password :"+password);
                final Animation atas = AnimationUtils.loadAnimation(this, R.anim.pindah_keatas);
                logo.startAnimation(atas);

            }catch (Exception e){
            }
        }
    }

    @OnClick(R.id.keluar)
    void logout(){
        mAuth.signOut();
        sesi = new SessionManager(getApplicationContext());
        sesi.logoutUser();
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            moveTaskToBack(true);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
