package com.example.myapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	
	// Поля
	static final String CONTACT_ID = "_id";
	static final String CONTACT_NAME = "first_name";
	static final String CONTACT_PHONE = "phone";

	private static final String AUTHORITY = "com.example.myapp.ContactProvider";

	private static final String CONTACT_PATH = "people";

	private static final Uri CONTACT_URI = Uri.parse("content://"
	      + AUTHORITY + "/" + CONTACT_PATH);
	
	SimpleCursorAdapter adapter;
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "OnCreate");
		setContentView(R.layout.activity_main);
		
		/*Cursor cursor = getContentResolver().query(CONTACT_URI, null, null,
		        null, null);*/
		
		//startManagingCursor(cursor);
		Intent intentP = new Intent(this, UpdateService.class);
		intentP.putExtra("name", "Steve Jobs");
		alarmIntent = PendingIntent.getService(this, 0, intentP, 0);
		alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmMgr.setInexactRepeating(AlarmManager.RTC, 
				System.currentTimeMillis() + 10000, 10000, alarmIntent);
		
		EnableReciever();
		
		getSupportLoaderManager().initLoader(0, null, this);
		
		Button addBtn = (Button) findViewById(R.id.add);
		Button deleteBtn = (Button) findViewById(R.id.delete);
		Button updateBtn = (Button) findViewById(R.id.update);
		Button stopBtn = (Button) findViewById(R.id.stop);
		
		addBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ContentValues cv = new ContentValues();
		        cv.put(CONTACT_NAME, "name 4");
		        cv.put(CONTACT_PHONE, "email 4");
		        Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
		        Log.d(TAG, "insert, result Uri : " + newUri.toString());
		        //adapter.notifyDataSetChanged();
			}
		});
		
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = ContentUris.withAppendedId(CONTACT_URI, 3);
				int cnt = getContentResolver().delete(uri, null, null);
				Log.d(TAG, "delete, count = " + cnt);
				//adapter.notifyDataSetChanged();
			}
		});
		
		updateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ContentValues cv = new ContentValues();
		        cv.put(CONTACT_NAME, "name 5");
		        cv.put(CONTACT_PHONE, "email 5");
		        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
		        int cnt = getContentResolver().update(uri, cv, null, null);
		        Log.d(TAG, "update, count = " + cnt);
		        //adapter.notifyDataSetChanged();
			}
		});
		
		stopBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DisableReciever();
				if (alarmMgr!= null) {
				    alarmMgr.cancel(alarmIntent);
				}
			}
		});
		
		String from[] = { CONTACT_NAME, CONTACT_PHONE };
		int to[] = { android.R.id.text1, android.R.id.text2 };
		adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, null, from, to, 0);

		ListView lvContact = (ListView) findViewById(R.id.contactsListView);
		lvContact.setAdapter(adapter);

		// SQLiteDatabase db = new ContactDbHelper(this).getReadableDatabase();
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { CONTACT_ID, CONTACT_NAME, CONTACT_PHONE };
	    CursorLoader cursorLoader = new CursorLoader(this,
	        CONTACT_URI, projection, null, null, null);
	    return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		 adapter.changeCursor(data);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		 adapter.changeCursor(null);
		
	}
	
	private void EnableReciever(){
		ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
		PackageManager pm = getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		        PackageManager.DONT_KILL_APP);
	}
	
	private void DisableReciever(){
		ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
		PackageManager pm = getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);
	}
	
}
