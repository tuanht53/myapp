package com.example.myapp.sync;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.myapp.db.ServerContract;
import com.example.myapp.db.CpContract.CpRevisionContract;
import com.example.myapp.network.NetHelper;
import common.Revision;

public class RevisionUpdater extends Updater{
	
	private static final String TAG = RevisionUpdater.class.getSimpleName();
	
	public RevisionUpdater(ContextWrapper cw){
			super(cw);
	}

	public void update() {
		revUpdate(getRevisionListFromBase(), getRevisionListFromServer());
	}

	private List<Revision> getRevisionListFromBase() {
		List<Revision> revisionBaseList = new ArrayList<Revision>();
		Cursor revCursor = mContext.getContentResolver().query(CpRevisionContract.REVISION_CONTENT_URI, null, null, null, null);
		if (revCursor.moveToFirst()) {
			int i = 0;
			do {
				i++;
				Log.d(TAG, "Запись в базе" + i);
				revisionBaseList.add(new Revision(revCursor));
			} while (revCursor.moveToNext());
		}
		revCursor.close();
		return revisionBaseList;
	}

	private List<Revision> getRevisionListFromServer() {
		NetHelper a = NetHelper.getInstance(mContext);
		String jsonBody = a.asyncGet(ServerContract.COLLECTIONS);
		JSONObject jObj = null;
		List<Revision> revisionServerList = new ArrayList<Revision>();
		try {
			jObj = new JSONObject(jsonBody);
			JSONObject response = jObj.getJSONObject("response");
			JSONArray names = response.names();
			for (int i=0; i<names.length(); i++){
				revisionServerList.add(new Revision(names.getString(i), 
													response.getJSONObject(names.getString(i)).getInt("rev")));
				Log.d(TAG, names.getString(i));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return revisionServerList;
	}
	
	private Revision findRevisionByName(Revision revToFind, List<Revision> revListWhereFind) {
		for (Revision r : revListWhereFind){
			if(revToFind.name.equals(r.name)) return revToFind;
 		}
		Log.d(TAG, "В искомой коллекции нет revision с name = " + revToFind.name);
		return null;
	}

	private void revUpdate(List<Revision> revisionBaseList, List<Revision> revisionServerList) {
		ContentValues cv = new ContentValues();
		for (Revision r : revisionServerList){
			Revision fRev = findRevisionByName(r, revisionBaseList);
			// add revision if base don't have it
			if (fRev == null) {
				cv.put("name", r.name);
				cv.put("revision", r.revision);
				mContext.getContentResolver().insert(CpRevisionContract.REVISION_CONTENT_URI, cv);
				next(r.name);
				
			// update revision if server have new revision
			}else if(r.revision > fRev.revision){ 
				cv.put("name", r.name);
				cv.put("revision", r.revision); 
				Uri updateUri = Uri.parse(CpRevisionContract.REVISION_CONTENT_URI.toString() + "/" + r.name);
				mContext.getContentResolver().update(updateUri, cv, null, null);
				next(r.name);
			}
		}
		
		for (Revision r : revisionBaseList){
			Revision fRev = findRevisionByName(r, revisionServerList);
			// delete revision if server don't have it
			if (fRev == null) {
				Uri deleteUri = Uri.parse(CpRevisionContract.REVISION_CONTENT_URI.toString() + "/" + r.name);
				mContext.getContentResolver().delete(deleteUri, null, null);
				next(r.name);
			}
		}
	}
	
	private void next(String revisionName){
		Log.d(TAG, revisionName);
		if(revisionName.equals("city")){
			CityEntity entity = new CityEntity(mContext);
			entity.performUpdate();
		}else if(revisionName.equals("office")){
			OfficeEntity entity = new OfficeEntity(mContext);
			entity.performUpdate();
		}
		
	}
}
