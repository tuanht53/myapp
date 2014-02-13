package com.example.myapp.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapp.SampleBootReceiver;
import com.example.myapp.update.UpdateService;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();

	public MainActivity() {
		Log.d(TAG, "MainConstructor");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.activity_main);

		firstRunSets();

		ListView lv = (ListView) findViewById(R.id.first_listview);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.menu_names, android.R.layout.simple_list_item_1);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = ((TextView) view);
				Intent intent;
				if (tv.getText().toString().equals("Ревизии")) {
					intent = new Intent(MainActivity.this,
							RevisionsActivity.class);
				} else if (tv.getText().toString().equals("Города")) {
					intent = new Intent(MainActivity.this, CitiesActivity.class);
				} else if (tv.getText().toString().equals("Офисы")) {
					intent = new Intent(MainActivity.this,
							OfficesActivity.class);
				} else if (tv.getText().toString().equals("Настройки")) {
					intent = new Intent(MainActivity.this,
							SettingsActivity.class);
				} else
					intent = new Intent(MainActivity.this,
							RevisionsActivity.class);

				startActivity(intent);
			}

		});
	}

	private void firstRunSets() {
		SharedPreferences prefs;
		prefs = getPreferences(MODE_PRIVATE);
		if (prefs.getBoolean("firstrun", true)) {
			doSets();
			prefs.edit().putBoolean("firstrun", false).commit();
		}
	}

	private void doSets() {
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		setDefaultAlarmUpdate();
		enableBootReciever();
	}

	private void setDefaultAlarmUpdate() {
		AlarmManager alarmMgr;
		PendingIntent alarmIntent;
		Intent intentP = new Intent(this, UpdateService.class);
		alarmIntent = PendingIntent.getService(this, 0, intentP, 0);
		alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		long syncPeriod = Long.valueOf(getString(R.string.default_sync_period));
		alarmMgr.setInexactRepeating(AlarmManager.RTC,
				System.currentTimeMillis() + syncPeriod, syncPeriod,
				alarmIntent);
	}

	private void enableBootReciever() {
		ComponentName receiver = new ComponentName(this,
				SampleBootReceiver.class);
		PackageManager pm = getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

}
