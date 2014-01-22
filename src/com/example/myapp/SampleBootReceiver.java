package com.example.myapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SampleBootReceiver extends BroadcastReceiver {

	private static final String TAG = SampleBootReceiver.class.getSimpleName();
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			Log.d(TAG, "onReceive = action.BOOT");
			Intent intentP = new Intent(context, UpdateService.class);
			intentP.putExtra("name", "Bill Gates");
			alarmIntent = PendingIntent.getService(context, 0, intentP, 0);
			alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmMgr.setInexactRepeating(AlarmManager.RTC, 
					System.currentTimeMillis() + 10000, 10000, alarmIntent);
		}else {
			Log.d(TAG, "onReceive = rest");
		}
	}

}
