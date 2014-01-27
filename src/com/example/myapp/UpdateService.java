package com.example.myapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.IntentService;
import android.content.ContentUris;
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
		/*String dataString = workIntent.getStringExtra("name");

		ContentValues cv = new ContentValues();
		cv.put(CONTACT_NAME, dataString);
		cv.put(CONTACT_PHONE, "phone");*/
		ArrayList<Person> mList = populate();
		for(int i=0;i<10;i++){
			ContentValues cv = new ContentValues();
			cv.put(CONTACT_NAME, mList.get(i).name);
			cv.put(CONTACT_PHONE, mList.get(i).phone);
			Uri uri = ContentUris.withAppendedId(CONTACT_URI, i);
			int cnt = getContentResolver().update(uri, cv, null, null);
		}
	}
	
	private ArrayList<Person> populate(){
		ArrayList<Person> mList = new ArrayList<Person>();
		for(int i=0;i<10;i++){
			mList.add(new Person("person# "+i, "phone# "+(9-i)));
		}
		Collections.shuffle(mList);
		return mList;
	}
	
	public class Person {
		private String name;
		private String phone;
		Person(String name, String phone){
			this.name = name;
			this.phone = phone;
		}
	}

}
