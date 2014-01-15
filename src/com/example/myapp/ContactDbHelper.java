package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDbHelper extends SQLiteOpenHelper {
	
	private static final String TAG = ContactDbHelper.class.getSimpleName();

	private static final String DB_CONTACTS = "contacts.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "people";
	private static final String NAME = "first_name";
	private static final String PHONE = "phone";
	private static final String TABLE_CREATE = "CREATE TABLE "
			+ TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, "
			+ PHONE + " TEXT);";

	ContactDbHelper(Context context) {
		super(context, DB_CONTACTS, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "OnCreate");
		db.execSQL(TABLE_CREATE);
		ContentValues values = new ContentValues();
		
		values.put(NAME, "DAVID");
		values.put(PHONE, "123456789");
		db.insert(TABLE_NAME, NAME, values);
		
		values.put(NAME, "MICHAEL");
		values.put(PHONE, "987654321");
		db.insert(TABLE_NAME, NAME, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
