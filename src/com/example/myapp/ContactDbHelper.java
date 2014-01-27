package com.example.myapp;

import java.util.ArrayList;
import java.util.Collections;

import com.example.myapp.UpdateService.Person;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
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
		/*ContentValues values = new ContentValues();
		
		values.put(NAME, "DAVID");
		values.put(PHONE, "123456789");
		db.insert(TABLE_NAME, NAME, values);
		
		values.put(NAME, "MICHAEL");
		values.put(PHONE, "987654321");
		db.insert(TABLE_NAME, NAME, values);*/
		ArrayList<Person> mList = populate();
		for(int i=0;i<10;i++){
			ContentValues cv = new ContentValues();
			cv.put(NAME, mList.get(i).name);
			cv.put(PHONE, mList.get(i).phone);
			db.insert(TABLE_NAME, NAME, cv);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
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
