package com.example.irhabi_ecsboard.sendbird.main;


import android.app.Application;

import com.sendbird.android.SendBird;

public class BaseApplication extends Application {

    private static final String APP_ID = "7BEFB8EB-01DE-41C6-8942-60D66B1F0564"; // US-1 Demo
    public static final String VERSION = "3.0.39";

    @Override
    public void onCreate() {
        super.onCreate();
        SendBird.init(APP_ID, getApplicationContext());
    }
}
