package com.example.myapp;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class UpdateService extends IntentService {

	private static final String TAG = MainActivity.class.getSimpleName();

	// Поля
	static final String CONTACT_ID = "_id";
	static final String CONTACT_NAME = "first_name";
	static final String CONTACT_PHONE = "phone";

	private static final String AUTHORITY = "com.example.myapp.ContactProvider";

	private static final String CONTACT_PATH = "people";

	private static final Uri CONTACT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + CONTACT_PATH);

	public UpdateService() {
		super("UpdateService");
		Log.d(TAG, "Constructor");

	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		Log.d(TAG, "onHandleIntent");
		String dataString = workIntent.getStringExtra("name");

		ContentValues cv = new ContentValues();
		cv.put(CONTACT_NAME, dataString);
		cv.put(CONTACT_PHONE, "phone");
		Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
	}

}
