package com.example.myapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.myapp.db.ServerContract;
import com.example.myapp.sync.UpdateService;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	public static final String KEY_PREF_AUTO_UPDATE = "pref_auto_update";
	public static final String KEY_PREF_COUNTRY_LIST = "pref_country_list";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(KEY_PREF_AUTO_UPDATE)) {

			Toast toast = Toast.makeText(getActivity(), "UpdateChanged",
					Toast.LENGTH_SHORT);
			toast.show();

			Intent intentP = new Intent(getActivity(), UpdateService.class);
			intentP.putExtra("name", "Steve Jobs");

			AlarmManager alarmMgr;
			PendingIntent alarmIntent;
			alarmIntent = PendingIntent
					.getService(getActivity(), 0, intentP, 0);
			alarmMgr = (AlarmManager) getActivity().getSystemService(
					getActivity().ALARM_SERVICE);

			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			boolean autoUpdate = sharedPref.getBoolean(KEY_PREF_AUTO_UPDATE,
					true);
			if (autoUpdate) {
				long syncPeriod = Long
						.valueOf(getString(R.string.default_sync_period));
				alarmMgr.setInexactRepeating(AlarmManager.RTC,
						System.currentTimeMillis() + syncPeriod, syncPeriod,
						alarmIntent);
				enableReciever();
			} else {
				alarmMgr.cancel(alarmIntent);
				disableReciever();
			}

		} else if (key.equals(KEY_PREF_COUNTRY_LIST)) {
			ListPreference listPreference = (ListPreference) findPreference(key);
			listPreference.setSummary(listPreference.getEntry());
			ServerContract.setCountry(listPreference.getEntry().toString());
            Toast toast = Toast.makeText(getActivity(), "CountryChanged",
					Toast.LENGTH_SHORT);
			toast.show();
			
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
	
	private void enableReciever(){
		ComponentName receiver = new ComponentName(getActivity(), SampleBootReceiver.class);
		PackageManager pm = getActivity().getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		        PackageManager.DONT_KILL_APP);
	}
	
	private void disableReciever(){
		ComponentName receiver = new ComponentName(getActivity(), SampleBootReceiver.class);
		PackageManager pm = getActivity().getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);
	}
	

}