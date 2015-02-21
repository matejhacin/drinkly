package com.matejhacin.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.matejhacin.stayhydrated.MainActivity;

public class BootReceiver extends BroadcastReceiver {

    AlarmReceiver alarm = new AlarmReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setAlarm(context, MainActivity.DEFAULT_INTERVAL_DURATION);
        }
    }
}
