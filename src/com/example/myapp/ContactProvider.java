package com.example.myapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ContactProvider extends ContentProvider {
	
	private static final String TAG = ContactProvider.class.getSimpleName();
	
	// Таблица
	private static final String CONTACT_TABLE = "people";
	
	// Поля
	static final String CONTACT_ID = "_id";
	static final String CONTACT_NAME = "first_name";
	static final String CONTACT_PHONE = "phone";

	private static final String AUTHORITY = "com.example.myapp.ContactProvider";

	private static final String CONTACT_PATH = "people";

	private static final Uri CONTACT_CONTENT_URI = Uri.parse("content://"
	      + AUTHORITY + "/" + CONTACT_PATH);
	
	// //UriMatcher
	// общий Uri
	static final int URI_CONTACTS = 1;

	// Uri с указанным ID
	static final int URI_CONTACTS_ID = 2;

	// описание и создание UriMatcher
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_CONTACTS);
		uriMatcher.addURI(AUTHORITY, CONTACT_PATH + "/#", URI_CONTACTS_ID);
	}
	
	private SQLiteDatabase db;
	private ContactDbHelper dbHelper;
	
	@Override
	public boolean onCreate() {
		Log.d(TAG, "onCreate");
	    dbHelper = new ContactDbHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
		      String[] selectionArgs, String sortOrder) {
	    Log.d(TAG, "query, " + uri.toString());
	    // проверяем Uri
	    switch (uriMatcher.match(uri)) {
	    case URI_CONTACTS: // общий Uri
	      Log.d(TAG, "URI_CONTACTS");
	      // если сортировка не указана, ставим свою - по имени
	      if (TextUtils.isEmpty(sortOrder)) {
	        sortOrder = CONTACT_NAME + " ASC";
	      }
	      break;
	    case URI_CONTACTS_ID: // Uri с ID
	      String id = uri.getLastPathSegment();
	      Log.d(TAG, "URI_CONTACTS_ID, " + id);
	      // добавляем ID к условию выборки
	      if (TextUtils.isEmpty(selection)) {
	        selection = CONTACT_ID + " = " + id;
	      } else {
	        selection = selection + " AND " + CONTACT_ID + " = " + id;
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Wrong URI: " + uri);
	    }
	    db = dbHelper.getWritableDatabase();
	    Cursor cursor = db.query(CONTACT_TABLE, projection, selection,
	        selectionArgs, null, null, sortOrder);
	    // просим ContentResolver уведомлять этот курсор 
	    // об изменениях данных в CONTACT_CONTENT_URI
	    cursor.setNotificationUri(getContext().getContentResolver(),
	        CONTACT_CONTENT_URI);
	    return cursor;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "insert, " + uri.toString());
	    if (uriMatcher.match(uri) != URI_CONTACTS)
	      throw new IllegalArgumentException("Wrong URI: " + uri);

	    db = dbHelper.getWritableDatabase();
	    long rowID = db.insert(CONTACT_TABLE, null, values);
	    Uri resultUri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, rowID);
	    // уведомляем ContentResolver, что данные по адресу resultUri изменились
	    getContext().getContentResolver().notifyChange(resultUri, null);
	    return resultUri;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
		      String[] selectionArgs) {
	    Log.d(TAG, "update, " + uri.toString());
	    switch (uriMatcher.match(uri)) {
	    case URI_CONTACTS:
	      Log.d(TAG, "URI_CONTACTS");

	      break;
	    case URI_CONTACTS_ID:
	      String id = uri.getLastPathSegment();
	      Log.d(TAG, "URI_CONTACTS_ID, " + id);
	      if (TextUtils.isEmpty(selection)) {
	        selection = CONTACT_ID + " = " + id;
	      } else {
	        selection = selection + " AND " + CONTACT_ID + " = " + id;
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Wrong URI: " + uri);
	    }
	    db = dbHelper.getWritableDatabase();
	    int cnt = db.update(CONTACT_TABLE, values, selection, selectionArgs);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return cnt;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(TAG, "delete, " + uri.toString());
	    switch (uriMatcher.match(uri)) {
	    case URI_CONTACTS:
	      Log.d(TAG, "URI_CONTACTS");
	      break;
	    case URI_CONTACTS_ID:
	      String id = uri.getLastPathSegment();
	      Log.d(TAG, "URI_CONTACTS_ID, " + id);
	      if (TextUtils.isEmpty(selection)) {
	        selection = CONTACT_ID + " = " + id;
	      } else {
	        selection = selection + " AND " + CONTACT_ID + " = " + id;
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Wrong URI: " + uri);
	    }
	    db = dbHelper.getWritableDatabase();
	    int cnt = db.delete(CONTACT_TABLE, selection, selectionArgs);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return cnt;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
