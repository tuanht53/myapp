package com.example.myapp.data;


import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapp.R;
import com.example.myapp.data.DbContract.CityTable;
import com.example.myapp.data.DbContract.OfficeTable;
import com.example.myapp.data.DbContract.RevisionTable;

/**
 * Provides access to a database entities of INVITRO. Description of entities can be found
 * in DbContract class.
 */
public class DbContentProvider extends ContentProvider implements
					OnSharedPreferenceChangeListener {
	
	// Used for debugging and logging
	private static final String TAG = DbContentProvider.class.getSimpleName();
	
	private static final String AUTHORITY = CpContract.AUTHORITY;

	private static final String CITY_PATH = CityTable.CITY_TABLE_NAME;

	private static final Uri CITY_CONTENT_URI = Uri.parse("content://"
	      + AUTHORITY + "/" + CITY_PATH);
	
	private static final String OFFICE_PATH = OfficeTable.OFFICE_TABLE_NAME;
	
	private static final Uri OFFICE_CONTENT_URI = Uri.parse("content://"
		      + AUTHORITY + "/" + OFFICE_PATH);
	
	private static final String REVISION_PATH = RevisionTable.REVISION_TABLE_NAME;
	
	private static final Uri REVISION_CONTENT_URI = Uri.parse("content://"
		      + AUTHORITY + "/" + REVISION_PATH);
	
	// //UriMatcher
	// общий Uri
	static final int URI_CITIES = 1;

	// Uri с указанным ID
	static final int URI_CITY_ID = 2;
	
	static final int URI_OFFICES = 3;

	static final int URI_OFFICE_ID = 4;
	
	static final int URI_REVISIONS = 5;

	static final int URI_REVISION_NAME = 6;

	// описание и создание UriMatcher
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, CITY_PATH, URI_CITIES);
		uriMatcher.addURI(AUTHORITY, CITY_PATH + "/#", URI_CITY_ID);
		uriMatcher.addURI(AUTHORITY, OFFICE_PATH, URI_OFFICES);
		uriMatcher.addURI(AUTHORITY, OFFICE_PATH + "/#", URI_OFFICE_ID);
		uriMatcher.addURI(AUTHORITY, REVISION_PATH, URI_REVISIONS);
		uriMatcher.addURI(AUTHORITY, REVISION_PATH + "/*", URI_REVISION_NAME);
	}
	
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	private SharedPreferences mPrefs; 
	private HashMap<String, DbHelper> dbMap = new HashMap<String, DbHelper>(); 
	
	@Override
	public boolean onCreate() {
		Log.d(TAG, "onCreate");
		dbHelper = new DbHelper(getContext());
		dbMap.put(getContext().getSharedPreferences("com.example.myapp_preferences", Context.MODE_PRIVATE)
    			.getString(getContext().getString(R.string.pref_country_list), "ru"), dbHelper);
	    mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
	    mPrefs.registerOnSharedPreferenceChangeListener(this);
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
		      String[] selectionArgs, String sortOrder) {
	    Log.d(TAG, "query, " + uri.toString());
	    db = dbHelper.getWritableDatabase();
	    Cursor cursor;
	    // проверяем Uri
	    switch (uriMatcher.match(uri)) {
	    case URI_CITIES: // общий Uri
	      Log.d(TAG, "URI_CITIES");
	      // если сортировка не указана, ставим свою - по имени
	      if (TextUtils.isEmpty(sortOrder)) {
	        //sortOrder = CONTACT_NAME + " ASC";
	      }
	      cursor = db.query(CityTable.CITY_TABLE_NAME, projection, selection,
	  	        selectionArgs, null, null, sortOrder);
	  	    // просим ContentResolver уведомлять этот курсор 
	  	    // об изменениях данных в CONTACT_CONTENT_URI
	  	    cursor.setNotificationUri(getContext().getContentResolver(),
	  	        CITY_CONTENT_URI);
	      break;
	    case URI_CITY_ID: // Uri с ID
	      String city_id = uri.getLastPathSegment();
	      Log.d(TAG, "URI_CITY_ID, " + city_id);
	      // добавляем ID к условию выборки
	      if (TextUtils.isEmpty(selection)) {
	        selection = CityTable.COLUMN_NAME_CITY_ID + " = " + city_id;
	      } else {
	        selection = selection + " AND " + CityTable.COLUMN_NAME_CITY_ID + " = " + city_id;
	      }
	      cursor = db.query(CityTable.CITY_TABLE_NAME, projection, selection,
	  	        selectionArgs, null, null, sortOrder);
	  	    // просим ContentResolver уведомлять этот курсор 
	  	    // об изменениях данных в CONTACT_CONTENT_URI
	  	    cursor.setNotificationUri(getContext().getContentResolver(),
	  	        CITY_CONTENT_URI);
	      break;
		case URI_OFFICES: // Uri с ID
			Log.d(TAG, "URI_OFFICES");
		      // если сортировка не указана, ставим свою - по имени
		      if (TextUtils.isEmpty(sortOrder)) {
		        //sortOrder = CONTACT_NAME + " ASC";
		      }
		      cursor = db.query(OfficeTable.OFFICE_TABLE_NAME, projection, selection,
		  	        selectionArgs, null, null, sortOrder);
		  	    // просим ContentResolver уведомлять этот курсор 
		  	    // об изменениях данных в CONTACT_CONTENT_URI
		  	    cursor.setNotificationUri(getContext().getContentResolver(),
		  	        OFFICE_CONTENT_URI);
		      break;
		case URI_OFFICE_ID: // Uri с ID
			String office_id = uri.getLastPathSegment();
			Log.d(TAG, "URI_OFFICE_ID, " + office_id);
			// добавляем ID к условию выборки
			if (TextUtils.isEmpty(selection)) {
				selection = OfficeTable.COLUMN_NAME_OFFICE_ID + " = " + office_id;
			} else {
				selection = selection + " AND " + OfficeTable.COLUMN_NAME_OFFICE_ID + " = " + office_id;
			}
			cursor = db.query(OfficeTable.OFFICE_TABLE_NAME, projection, selection,
		  	        selectionArgs, null, null, sortOrder);
		  	    // просим ContentResolver уведомлять этот курсор 
		  	    // об изменениях данных в CONTACT_CONTENT_URI
		  	    cursor.setNotificationUri(getContext().getContentResolver(),
		  	        OFFICE_CONTENT_URI);
			break;
		case URI_REVISIONS: // общий Uri
		      Log.d(TAG, "URI_REVISIONS");
		      if (TextUtils.isEmpty(sortOrder)) {
		        //sortOrder = CONTACT_NAME + " ASC";
		      }
		      cursor = db.query(RevisionTable.REVISION_TABLE_NAME, projection, selection,
		  	        selectionArgs, null, null, sortOrder);
		      
		      Log.d(TAG, "Count rows = " + cursor.getCount());
		  	    // просим ContentResolver уведомлять этот курсор 
		  	    // об изменениях данных в CONTACT_CONTENT_URI
		  	  cursor.setNotificationUri(getContext().getContentResolver(),
		  	        REVISION_CONTENT_URI);
		      break;
		    case URI_REVISION_NAME: // Uri с ID
		      String revision_name = uri.getLastPathSegment();
		      if (TextUtils.isEmpty(selection)) {
		        selection = RevisionTable.COLUMN_NAME_REVISION_NAME + "='" + revision_name + "'";
		      } else {
		        selection = selection + " AND " + RevisionTable.COLUMN_NAME_REVISION_NAME + "='" + revision_name + "'";
		      }
		      cursor = db.query(RevisionTable.REVISION_TABLE_NAME, projection, selection,
		  	        selectionArgs, null, null, sortOrder);
		  	    // просим ContentResolver уведомлять этот курсор 
		  	    // об изменениях данных в CONTACT_CONTENT_URI
		  	    cursor.setNotificationUri(getContext().getContentResolver(),
		  	        REVISION_CONTENT_URI);
		      break;	
		default:
	      throw new IllegalArgumentException("Wrong URI: " + uri);
	    }
	   
	    return cursor;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "insert, " + uri.toString());
		db = dbHelper.getWritableDatabase();
		Uri resultUri;
	    if ((uriMatcher.match(uri) != URI_CITIES)&&
	    		(uriMatcher.match(uri) != URI_REVISIONS)&&
	    			(uriMatcher.match(uri) != URI_OFFICES))
	      throw new IllegalArgumentException("Wrong URI: " + uri);

	    switch (uriMatcher.match(uri)) {
		case URI_CITIES:
			long rowID = db.insert(CityTable.CITY_TABLE_NAME, null, values);
			resultUri = ContentUris.withAppendedId(CITY_CONTENT_URI, rowID);
			// уведомляем ContentResolver, что данные по адресу resultUri
			// изменились
			getContext().getContentResolver().notifyChange(resultUri, null);

			break;
		case URI_REVISIONS: // общий Uri
			long revID = db.insert(RevisionTable.REVISION_TABLE_NAME, null, values);
			resultUri = ContentUris.withAppendedId(REVISION_CONTENT_URI, revID);
			// уведомляем ContentResolver, что данные по адресу resultUri
			// изменились
			getContext().getContentResolver().notifyChange(resultUri, null);
			break;
		case URI_OFFICES: // общий Uri
			long offID = db.insert(OfficeTable.OFFICE_TABLE_NAME, null, values);
			resultUri = ContentUris.withAppendedId(OFFICE_CONTENT_URI, offID);
			// уведомляем ContentResolver, что данные по адресу resultUri
			// изменились
			getContext().getContentResolver().notifyChange(resultUri, null);
			break;
	    default:
		      throw new IllegalArgumentException("Wrong URI: " + uri);
		}
	   
	    return resultUri;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
		      String[] selectionArgs) {
	    Log.d(TAG, "update, " + uri.toString());
	    db = dbHelper.getWritableDatabase();
	    int cnt = 0;
	    switch (uriMatcher.match(uri)) {
	    case URI_CITIES:
	      Log.d(TAG, "URI_CONTACTS");

	      break;
	    case URI_CITY_ID:
	      String city_id = uri.getLastPathSegment();
	      Log.d(TAG, "URI_CONTACTS_ID, " + city_id);
	      if (TextUtils.isEmpty(selection)) {
	        selection = CityTable.COLUMN_NAME_CITY_ID + " = " + city_id;
	      } else {
	        selection = selection + " AND " + CityTable.COLUMN_NAME_CITY_ID + " = " + city_id;
	      }
	      cnt = db.update(CityTable.CITY_TABLE_NAME, values, selection, selectionArgs);
	      break;
		case URI_OFFICES:
			Log.d(TAG, "URI_CONTACTS");

			break;
		case URI_OFFICE_ID:
			String office_id = uri.getLastPathSegment();
			Log.d(TAG, "URI_CONTACTS_ID, " + office_id);
			if (TextUtils.isEmpty(selection)) {
				selection = OfficeTable.COLUMN_NAME_OFFICE_ID + " = " + office_id;
			} else {
				selection = selection + " AND " + OfficeTable.COLUMN_NAME_OFFICE_ID + " = " + office_id;
			}
			cnt = db.update(OfficeTable.OFFICE_TABLE_NAME, values, selection, selectionArgs);
		    break;
		case URI_REVISIONS:
			Log.d(TAG, "URI_REVISIONS");

			break;
		case URI_REVISION_NAME:
			String revision_name = uri.getLastPathSegment();
			Log.d(TAG, "URI_REVISION_ID, " + revision_name);
			if (TextUtils.isEmpty(selection)) {
				selection = RevisionTable.COLUMN_NAME_REVISION_NAME + "='" + revision_name + "'";
			} else {
				selection = selection + " AND " + RevisionTable.COLUMN_NAME_REVISION_NAME + "='" + revision_name + "'";
			}
			cnt = db.update(RevisionTable.REVISION_TABLE_NAME, values, selection, selectionArgs);
			break;
	    default:
	      throw new IllegalArgumentException("Wrong URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return cnt;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(TAG, "delete, " + uri.toString());
		db = dbHelper.getWritableDatabase();
		String tableName = null;
	    switch (uriMatcher.match(uri)) {
	    case URI_CITIES:
	      Log.d(TAG, "URI_CONTACTS");
	      break;
	    case URI_CITY_ID:
	      String city_id = uri.getLastPathSegment();
	      Log.d(TAG, "URI_CONTACTS_ID, " + city_id);
	      if (TextUtils.isEmpty(selection)) {
	        selection = CityTable.COLUMN_NAME_CITY_ID + " = " + city_id;
	      } else {
	        selection = selection + " AND " + CityTable.COLUMN_NAME_CITY_ID + " = " + city_id;
	      }
	      tableName = CityTable.CITY_TABLE_NAME;
	      break;
		case URI_REVISIONS:
			Log.d(TAG, "URI_CONTACTS");
			break;
		case URI_REVISION_NAME:
			String revision_name = uri.getLastPathSegment();
			Log.d(TAG, "URI_CONTACTS_ID, " + revision_name);
			if (TextUtils.isEmpty(selection)) {
				selection = RevisionTable.COLUMN_NAME_REVISION_NAME + "='" + revision_name + "'";
			} else {
				selection = selection + " AND " + RevisionTable.COLUMN_NAME_REVISION_NAME
						+ "='" + revision_name + "'";
			}
			tableName = RevisionTable.REVISION_TABLE_NAME;
			break;
		case URI_OFFICES:
			Log.d(TAG, "URI_OFFICES");
			break;
		case URI_OFFICE_ID:
			String office_id = uri.getLastPathSegment();
			Log.d(TAG, "URI_OFFICES_ID, " + office_id);
			if (TextUtils.isEmpty(selection)) {
				selection = OfficeTable.COLUMN_NAME_OFFICE_ID + "='" + office_id + "'";
			} else {
				selection = selection + " AND " + OfficeTable.COLUMN_NAME_OFFICE_ID
						+ "='" + office_id + "'";
			}
			tableName = OfficeTable.OFFICE_TABLE_NAME;
			break;
	    default:
	      throw new IllegalArgumentException("Wrong URI: " + uri);
	    }
	   
	    int cnt = db.delete(tableName, selection, selectionArgs);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return cnt;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// stop service before change country
		//getContext().stopService(new Intent(getContext(), UpdateService.class));
		if (key.equals(getContext().getString(R.string.pref_country_list)))
			dbHelper = getDbHelper(sharedPreferences.getString(key, "ru"));
		Log.d(TAG, "ProviderOnSharedPreferenceChanged");
	}	
	
	private DbHelper getDbHelper(String countryCode){
		if (!dbMap.containsKey(countryCode))
			dbMap.put(countryCode, new DbHelper(getContext()));
		return dbMap.get(countryCode);
	}

}
