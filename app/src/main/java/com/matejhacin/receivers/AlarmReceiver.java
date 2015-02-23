package com.matejhacin.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.matejhacin.stayhydrated.R;

import java.util.Random;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    AlarmManager alarmManager;
    PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent piDrink = PendingIntent.getBroadcast(context, 0, new Intent(context, DrinkReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piLater = PendingIntent.getBroadcast(context, 0, new Intent(context, LaterReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.smallicon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setTicker("Time to drink!")
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(getRandomNotificationMessage(context))
                .addAction(R.drawable.drink_now, "Drink!", piDrink)
                .addAction(R.drawable.drink_later, "Later!", piLater)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOnlyAlertOnce(true);
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(1, nBuilder.build());
    }

    public void setAlarm(Context context, int milliseconds)
    {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);

        // Fire alarm every "milliseconds"
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + milliseconds,
                milliseconds,
                alarmIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context)
    {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);
        alarmManager.cancel(alarmIntent);

        // Disable BootReceiver so that alarm won't start again if device is rebooted
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public String getRandomNotificationMessage(Context context)
    {
        String[] messages = context.getResources().getStringArray(R.array.notification_messages);
        return messages[new Random().nextInt(messages.length)];
    }

}