package com.example.myapp.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.SampleBootReceiver;
import com.example.myapp.update.UpdateService;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	
	private static final String TAG = SettingsFragment.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.d(TAG, "onSharedPreferenceChange");
		if (key.equals(getString(R.string.pref_auto_update))) {

			Toast toast = Toast.makeText(getActivity(), "UpdateChanged", Toast.LENGTH_SHORT);
			toast.show();

			Intent intentP = new Intent(getActivity(), UpdateService.class);
			PendingIntent alarmIntent;
			alarmIntent = PendingIntent.getService(getActivity(), 0, intentP, 0);
			AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			boolean autoUpdate = sharedPref.getBoolean(getString(R.string.pref_auto_update), true);
			if (autoUpdate) {
				long syncPeriod = Long.valueOf(getString(R.string.default_sync_period));
				alarmMgr.setInexactRepeating(AlarmManager.RTC,
						System.currentTimeMillis() + syncPeriod, syncPeriod, alarmIntent);
				enableBootReciever();
			} else {
				alarmMgr.cancel(alarmIntent);
				disableBootReciever();
			}

		} else if (key.equals(getString(R.string.pref_country_list))) {
			
			ListPreference listPreference = (ListPreference) findPreference(key);
			listPreference.setSummary(listPreference.getEntry());
			
			SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(getString(R.string.pref_country_list), listPreference.getValue());
			editor.commit();
			
            Toast toast = Toast.makeText(getActivity(), "CountryChanged to " + listPreference.getValue(),
					Toast.LENGTH_SHORT);
			toast.show();
			
		}else if (key.equals(getString(R.string.pref_country_list_enabled))){
			ListPreference listPreference = (ListPreference) findPreference(getString(R.string.pref_country_list));
			boolean isEnabled = sharedPreferences.getBoolean(key, true);
			listPreference.setEnabled(isEnabled);
			if (!isEnabled)
				listPreference.setSummary("Идет обновление...");
			else
				listPreference.setSummary(listPreference.getEntry());
			
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setPrefs();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}
	
	private void setPrefs() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity()); 
		boolean isEnabled =  pref.getBoolean(getString(R.string.pref_country_list_enabled), true);
		getPreferenceScreen().findPreference(getString(R.string.pref_country_list)).setEnabled(isEnabled);
		ListPreference listPreference = (ListPreference) findPreference(getString(R.string.pref_country_list));
		listPreference.getEditor().remove(getString(R.string.pref_auto_update));
		if (!isEnabled)
			listPreference.setSummary("Идет обновление...");
		else
			listPreference.setSummary(listPreference.getEntry());
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
	
	private void enableBootReciever(){
		ComponentName receiver = new ComponentName(getActivity(), SampleBootReceiver.class);
		PackageManager pm = getActivity().getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		        PackageManager.DONT_KILL_APP);
	}
	
	private void disableBootReciever(){
		ComponentName receiver = new ComponentName(getActivity(), SampleBootReceiver.class);
		PackageManager pm = getActivity().getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);
	}
}