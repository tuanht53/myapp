package com.example.myapp;

import com.example.myapp.sync.UpdateService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class SampleBootReceiver extends BroadcastReceiver {

	private static final String TAG = SampleBootReceiver.class.getSimpleName();
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    		boolean autoUpdate = sharedPref.getBoolean("pref_auto_update", true);
    		if (autoUpdate){
    			Log.d(TAG, "onReceive = action.BOOT");
    			Intent intentP = new Intent(context, UpdateService.class);
    			intentP.putExtra("name", "Bill Gates");
    			alarmIntent = PendingIntent.getService(context, 0, intentP, 0);
    			alarmMgr = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
    			long syncPeriod = Long.valueOf(context.getString(R.string.default_sync_period));
    			alarmMgr.setInexactRepeating(AlarmManager.RTC, 
    					System.currentTimeMillis() + syncPeriod, syncPeriod, alarmIntent);
    		}else{
    			alarmMgr.cancel(alarmIntent);
    		}
    		
		}else {
			Log.d(TAG, "onReceive = rest");
		}
	}

}
