package com.example.irhabi_ecsboard.sendbird.fcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

/**
 * Improved by Andrian Latif
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        storeRegIdInPref(refreshedToken);

        sendRegistrationToServer(refreshedToken);
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        SendBird.registerPushTokenForCurrentUser(token, new SendBird.RegisterPushTokenWithStatusHandler() {
            @Override
            public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
                if (e != null) {
                    Toast.makeText(MyFirebaseInstanceIDService.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pushTokenRegistrationStatus == SendBird.PushTokenRegistrationStatus.PENDING) {
                    Toast.makeText(MyFirebaseInstanceIDService.this, "Connection required to register push token.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.e(TAG, "sendRegistrationToServer: " + token);
    }


    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}