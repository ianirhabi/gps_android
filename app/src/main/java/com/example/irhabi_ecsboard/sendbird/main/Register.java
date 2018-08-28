package com.example.irhabi_ecsboard.sendbird.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.irhabi_ecsboard.sendbird.R;
import com.example.irhabi_ecsboard.sendbird.sesi.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {

    @BindView(R.id.call)
    Button Call;

    @BindView(R.id.nomor)
    EditText num;

    @BindView(R.id.pass)
    EditText pas;

    @BindView(R.id.nama)
    EditText name;

    private FirebaseAuth mAuth;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.call)
    void call(){
        String nom = num.getText().toString();
        String nama = name.getText().toString();
        String pasword = pas.getText().toString();

        if(nom.isEmpty() || nama.isEmpty() || pasword.isEmpty()){
            num.setError("Tidak boleh kosong");
            name.setError("Tidak boleh kosong");
            pas.setError("Tidak boleh kosong");
        }else {
            Intent i = null;
            i = new Intent(Register.this, Authnumber.class);
            Bundle getNumber = new Bundle();
            getNumber.putString("parsing_number", nom);
            getNumber.putString("parsing_nama", nama);
            getNumber.putString("parsing_password", pasword);
            i.putExtras(getNumber);
            startActivity(i);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()== true)
        {
            Intent i = new Intent(Register.this, Welcome.class);
            startActivity(i);
        }
    }
}
