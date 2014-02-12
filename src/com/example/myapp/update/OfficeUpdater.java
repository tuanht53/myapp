package com.example.myapp.update;

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

import com.example.myapp.data.ServerContract;
import com.example.myapp.data.CpContract.CpOfficeContract;
import com.example.myapp.network.NetHelper;
import com.example.myapp.orm.Office;

public class OfficeUpdater extends Updater {
	
private static final String TAG = OfficeUpdater.class.getSimpleName();
	
	public OfficeUpdater(ContextWrapper cw) {
		super(cw);
	}

	@Override
	public void update() {
		Log.d(TAG, "update Office");
		officeUpdate(getOfficeListFromBase(), getOfficeListFromServer());
	}
	
	private List<Office> getOfficeListFromBase() {
		List<Office> officeBaseList = new ArrayList<Office>();
		Cursor officeCursor = mContext.getContentResolver().query(CpOfficeContract.OFFICE_CONTENT_URI, null, null, null, null);
		if (officeCursor.moveToFirst()) {
			int i = 0;
			do {
				i++;
				Log.d(TAG, "Запись в базе" + i);
				officeBaseList.add(new Office(officeCursor));
			} while (officeCursor.moveToNext());
		}
		officeCursor.close();
		return officeBaseList;
	}

	private List<Office> getOfficeListFromServer() {
		NetHelper a = NetHelper.getInstance(mContext);
		String jsonBody = a.asyncGet(ServerContract.getInstance(mContext).getOffice());
		JSONObject jObj = null;
		List<Office> officeServerList = new ArrayList<Office>();
		try {
			jObj = new JSONObject(jsonBody);
			JSONArray response = jObj.getJSONArray("response");
			for (int i=0; i<response.length(); i++){
				officeServerList.add(new Office(response.getJSONObject(i)));
				Log.d(TAG, response.getString(i));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return officeServerList;
	}
	
	private Office findOfficeById(Office officeToFind, List<Office> officeListWhereFind) {
		for (Office city : officeListWhereFind){
			if(officeToFind.office_id==city.office_id) return officeToFind;
 		}
		Log.d(TAG, "В искомой коллекции нет office с id = " + officeToFind.office_id);
		return null;
	}

	private void officeUpdate(List<Office> officeBaseList, List<Office> officeServerList) {
		ContentValues cv = new ContentValues();
		for (Office office : officeServerList){
			Office fOffice = findOfficeById(office, officeBaseList);
			// add city if base don't have it
			if (fOffice == null) {
				cv.put("name", office.name);
				cv.put("metro", office.metro);
				cv.put("address", office.address);
				cv.put("city_id", office.city_id); 
				cv.put("revision", office.revision); 
				cv.put("office_id", office.office_id); 
				mContext.getContentResolver().insert(CpOfficeContract.OFFICE_CONTENT_URI, cv);
				
			// update city if server have new revision
			}else if(office.revision > fOffice.revision){ 
				cv.put("name", office.name);
				cv.put("metro", office.metro);
				cv.put("address", office.address);
				cv.put("city_id", office.city_id); 
				cv.put("revision", office.revision); 
				cv.put("office_id", office.office_id); 
				Uri updateUri = Uri.parse(CpOfficeContract.OFFICE_CONTENT_URI.toString() + "/" + office.office_id);
				mContext.getContentResolver().update(updateUri, cv, null, null);
			}
		}
		
		for (Office office : officeBaseList){
			Office fOffice = findOfficeById(office, officeServerList);
			// delete city if server don't have it
			if (fOffice == null) {
				Uri deleteUri = Uri.parse(CpOfficeContract.OFFICE_CONTENT_URI.toString() + "/" + office.office_id);
				mContext.getContentResolver().delete(deleteUri, null, null);
			}
		}
	}

}
