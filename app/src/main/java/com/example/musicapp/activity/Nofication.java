package com.example.musicapp.activity;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Nofication extends Application {

    public static String CHANNEL_ID = "CHANNEL_ID_MUSIC";

    @Override
    public void onCreate() {
        super.onCreate();
        createNofication();
    }

    private void createNofication() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel music", NotificationManager.IMPORTANCE_DEFAULT);

            channel.setSound(null,null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

    }
}
