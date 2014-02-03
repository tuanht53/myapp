package com.example.myapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ListView;

import com.example.myapp.db.CpContract.CpCityContract;
import com.example.myapp.db.CpContract.CpRevisionContract;
import com.example.myapp.db.DbContract.CityTable;
import com.example.myapp.db.DbContract.RevisionTable;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	
	SimpleCursorAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "OnCreate");
		setContentView(R.layout.activity_main);
		
		getSupportLoaderManager().initLoader(0, null, this);
		
		String from[] = { RevisionTable.COLUMN_NAME_REVISION_NAME, RevisionTable.COLUMN_NAME_REVISION_REVISION };
		int to[] = { android.R.id.text1, android.R.id.text2 };
		adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, null, from, to, 0);

		ListView lvContact = (ListView) findViewById(R.id.contactsListView);
		lvContact.setAdapter(adapter);

		// SQLiteDatabase db = new ContactDbHelper(this).getReadableDatabase();
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { RevisionTable._ID, RevisionTable.COLUMN_NAME_REVISION_NAME, RevisionTable.COLUMN_NAME_REVISION_REVISION };
	    CursorLoader cursorLoader = new CursorLoader(this,
	        CpRevisionContract.REVISION_CONTENT_URI, projection, null, null, null);
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
	
}
