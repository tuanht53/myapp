package com.example.myapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapp.data.DbContract.CityTable;
import com.example.myapp.data.DbContract.OfficeTable;
import com.example.myapp.data.DbContract.RevisionTable;

public class DbHelper extends SQLiteOpenHelper{
	
	private static final String TAG = DbHelper.class.getSimpleName();

	DbHelper(Context context) {
		super(context, DbContract.getDbName(context), null, DbContract.DB_VERSION);
		Log.d(TAG, "Constructor");
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
