package com.example.myapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FirstListViewActivity extends Activity {
	
	private static final String TAG = FirstListViewActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_listview);
	
		SharedPreferences prefs;
		prefs = getPreferences(MODE_PRIVATE);
		if (prefs.getBoolean("firstrun", true)) {
            firstRun();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
		
		ListView lv = (ListView) findViewById(R.id.first_listview);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		        this, R.array.menu_names,
		        android.R.layout.simple_list_item_1);
	    lv.setAdapter(adapter);
	    lv.setOnItemClickListener(new OnItemClickListener(){
	    	@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	    		TextView tv = ((TextView)view);
	    		Intent intent;
	    		if (tv.getText().toString().equals("Главная")){
	    			intent = new Intent(FirstListViewActivity.this, MainActivity.class);
	    		}else if (tv.getText().toString().equals("Настройки")){
	    			intent = new Intent(FirstListViewActivity.this, SettingsActivity.class);
	    		}else 
	    			intent = new Intent(FirstListViewActivity.this, MainActivity.class);
	    		
	    		startActivity(intent);
			}

	    });
	}

	private void firstRun() {
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		setDefaultAlarmUpdate();
		enableReciever();
	}
	
	private void setDefaultAlarmUpdate(){
		AlarmManager alarmMgr;
		PendingIntent alarmIntent;
		Intent intentP = new Intent(this, UpdateService.class);
		intentP.putExtra("name", "Steve Jobs");
		alarmIntent = PendingIntent.getService(this, 0, intentP, 0);
		alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		long syncPeriod = Long.valueOf(getString(R.string.default_sync_period));
		alarmMgr.setInexactRepeating(AlarmManager.RTC, 
				System.currentTimeMillis() + syncPeriod, syncPeriod, alarmIntent);
	}
	
	private void enableReciever(){
		ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
		PackageManager pm = getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		        PackageManager.DONT_KILL_APP);
	}
	
	private void disableReciever(){
		ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
		PackageManager pm = getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);
	}
}
