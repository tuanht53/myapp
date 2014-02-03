package com.example.myapp.db;

import com.example.myapp.db.DbContract.CityTable;
import com.example.myapp.db.DbContract.OfficeTable;
import com.example.myapp.db.DbContract.RevisionTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	
	private static final String TAG = DbHelper.class.getSimpleName();

	DbHelper(Context context) {
		super(context, DbContract.DB_NAME, null, DbContract.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "OnCreate");
		db.execSQL(CityTable.CREATE_CITY_TABLE);
		db.execSQL(OfficeTable.CREATE_OFFICE_TABLE);
		db.execSQL(RevisionTable.CREATE_REVISION_TABLE);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CityTable.CITY_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + OfficeTable.OFFICE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + RevisionTable.REVISION_TABLE_NAME);
		onCreate(db);
	}
	
}
