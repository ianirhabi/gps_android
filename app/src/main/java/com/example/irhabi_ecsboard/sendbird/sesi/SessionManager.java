package com.example.irhabi_ecsboard.sendbird.sesi;

/**
 * Modified by irhabi on 10/31/2017.
 */


/**
 * Created by irhabi on 4/24/2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.example.irhabi_ecsboard.sendbird.main.LoginActivity;

import java.util.HashMap;

@SuppressLint("CommitPrefEdits")
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;
    int employ = 5;

    // nama sharedpreference
    private static final String PREF_NAME = "Sesi";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "nama";
    public static final String KEY_NAME = "id";
    public  static  final String KEY_PASSWORD = "grup";
    public  static  final String KEY_STATUS = "status";

    // public Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * ciptakan login sesion
     * */
    // hanya menyimpan email
    public void createRegisSession(String name, String username, String password){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public void createStatus(String status){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_STATUS, status);
        editor.commit();
    }


    /**
     *mendapatkan user by teknologi programmer jalanan
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME,null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_STATUS,pref.getString(KEY_STATUS,null));
        return user;
    }

    /**
     * Hapus sesi by programmer jalanan
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}