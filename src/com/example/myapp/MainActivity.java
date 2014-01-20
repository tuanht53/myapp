package com.example.myapp;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "OnCreate");
		setContentView(R.layout.activity_main);
		
		Cursor cursor = getContentResolver().query(CONTACT_URI, null, null,
		        null, null);
		
		startManagingCursor(cursor);
		
		Button addBtn = (Button) findViewById(R.id.add);
		Button deleteBtn = (Button) findViewById(R.id.delete);
		Button updateBtn = (Button) findViewById(R.id.update);
		
		addBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ContentValues cv = new ContentValues();
		        cv.put(CONTACT_NAME, "name 4");
		        cv.put(CONTACT_PHONE, "email 4");
		        Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
		        Log.d(TAG, "insert, result Uri : " + newUri.toString());
		        adapter.notifyDataSetChanged();
			}
		});
		
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = ContentUris.withAppendedId(CONTACT_URI, 3);
				int cnt = getContentResolver().delete(uri, null, null);
				Log.d(TAG, "delete, count = " + cnt);
				adapter.notifyDataSetChanged();
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
		        adapter.notifyDataSetChanged();
			}
		});
		
		String from[] = { "first_name", "phone" };
		int to[] = { android.R.id.text1, android.R.id.text2 };
		adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, cursor, from, to);

		ListView lvContact = (ListView) findViewById(R.id.contactsListView);
		lvContact.setAdapter(adapter);

		// SQLiteDatabase db = new ContactDbHelper(this).getReadableDatabase();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
