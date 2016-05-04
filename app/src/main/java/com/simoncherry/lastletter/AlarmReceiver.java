package com.simoncherry.lastletter;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setTicker("Greeting from LastLetter");
//        builder.setContentTitle("Are you still alive?");
//        builder.setContentText("Please Login LastLetter to update your status.");
//        builder.setDefaults(Notification.DEFAULT_ALL);
//        Notification notification = builder.build();
//        manager.notify(1, notification);

        //再次开启LongRunningService这个服务，从而可以
        Intent i = new Intent(context, PollingService.class);
        context.startService(i);
    }
}