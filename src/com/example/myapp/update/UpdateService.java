package com.example.myapp.update;

import java.util.ArrayList;
import java.util.List;

import com.example.myapp.R;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class UpdateService extends IntentService {

	private static final String TAG = UpdateService.class.getSimpleName();
	
	public UpdateService() {
		super("UpdateService");
		Log.d(TAG, "Constructor");
	}
	
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		Log.d(TAG, "onHandleIntent");
		updateProcess();
	}

	private void updateProcess() {
		// disable country change
		SharedPreferences.Editor prefEditor = PreferenceManager
				.getDefaultSharedPreferences(this).edit();
		prefEditor.putBoolean(getString(R.string.pref_country_list_enabled), false);
		prefEditor.commit();

		// do update
		List<Entity> entityList = new ArrayList<Entity>();
		entityList.add(new RevisionEntity(this));
		entityList.add(new CityEntity(this));
		for (Entity entity : entityList) {
			entity.performUpdate();
		}
		
		// enable country change
		prefEditor.putBoolean(getString(R.string.pref_country_list_enabled), true);  
		prefEditor.commit();

	}

}