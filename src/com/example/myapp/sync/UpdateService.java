package com.example.myapp.sync;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
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
		List<Entity> entityList = new ArrayList<Entity>(); 
		entityList.add(new RevisionEntity(this));
		for (Entity entity : entityList){
			entity.performUpdate();
 		}
	}
}