package com.example.irhabi_ecsboard.sendbird.anggota;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.irhabi_ecsboard.sendbird.R;
import com.example.irhabi_ecsboard.sendbird.admin.Admin;
import com.example.irhabi_ecsboard.sendbird.detailiuran.IuranDetail;
import com.example.irhabi_ecsboard.sendbird.dialog.Inputiuran;
import com.example.irhabi_ecsboard.sendbird.main.LoginActivity;
import com.example.irhabi_ecsboard.sendbird.main.MainActivity;
import com.example.irhabi_ecsboard.sendbird.model.Anggota;
import com.example.irhabi_ecsboard.sendbird.model.User;
import com.example.irhabi_ecsboard.sendbird.service.RetrofitInstance;
import com.example.irhabi_ecsboard.sendbird.service.Router;
import com.example.irhabi_ecsboard.sendbird.sesi.SessionManager;
import com.example.irhabi_ecsboard.sendbird.utils.PreferenceUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnggotaActivity extends AppCompatActivity implements AnggotaAdapter.AnggotaListener {

    private SearchView searchView;
    private AnggotaAdapter mAdapter;
    private RetrofitInstance service;
    private Router router;
    private RecyclerView recyclerView;
    private Inputiuran iurandialog;
    private SessionManager sesi;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // set an exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }
        setContentView(R.layout.activity_anggota);
        setFloatingButton();
        getAnggota();
    }

    public void setFloatingButton(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final String status = PreferenceUtils.getUserGrup(this);
        this.status = status;
        if (status.equals("4")) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(AnggotaActivity.this, MainActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(AnggotaActivity.this).toBundle());
                    }
                }
            });
        }else {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(AnggotaActivity.this, Admin.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(AnggotaActivity.this).toBundle());
                    }
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Anggota Gps");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_anggota, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        //listening to search text query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        if(id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * ketika barang di klik
     * @param user
     */

    @Override
    public void onAnggotaSelected(User user){

        if(status.equals("4")){

            Intent i = new Intent(AnggotaActivity.this, IuranDetail.class);
            Bundle ambildata = new Bundle();
            ambildata.putInt("id", user.getId());
            ambildata.putString("anggota", user.getUsername());
            ambildata.putString("nomor", user.getName());
            i.putExtras(ambildata);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(AnggotaActivity.this).toBundle());
            }

        }else {

            String[] st = {"selesai", "belum selesai"};
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item, st);
            sesi = new SessionManager(getApplicationContext());
            sesi.createStatus("1");
            iurandialog = new Inputiuran();
            iurandialog.showInput(AnggotaActivity.this, "",
                    AnggotaActivity.this, user.getId(), user.getName(), adapter, user.getUsername(), user.getImei());
        }
    }

    public void getAnggota(){

        service = new RetrofitInstance();

        router = service.getRetrofitInstanceall().create(Router.class);

        Call<Anggota> call = router.Get();
        call.enqueue(new Callback<Anggota>() {
            @Override
            public void onResponse(Call<Anggota> call, Response<Anggota> response) {
                if(response.body().getStatus().equals("berhasil")) {
                    Log.d("DEBUG masuk sini ","" + response.body().getData());
                    generateAnggota(response.body().getData());
                }else if(response.body().getStatus() == null){
                    Toast.makeText(getApplicationContext(),"data null",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Anggota> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"gagal menghubungi server ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateAnggota(ArrayList<User> anggota) {
        recyclerView = findViewById(R.id.recycler_view_anggota);
        mAdapter = new AnggotaAdapter(AnggotaActivity.this, anggota, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));

        recyclerView.setAdapter(mAdapter);
    }
}
