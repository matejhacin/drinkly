package com.matejhacin.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.matejhacin.stayhydrated.MainActivity;

public class LaterReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// Set AlarmManager to show again in 10min
		new AlarmReceiver().setAlarm(context, MainActivity.DEFAULT_POSTPONE_DURATION);

		// Delete the notification
		NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nManager.cancel(1);
	}
}
