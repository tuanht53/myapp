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
import com.example.myapp.data.CpContract.CpCityContract;
import com.example.myapp.network.NetHelper;
import com.example.myapp.orm.City;

public class CityUpdater extends Updater{
	
	private static final String TAG = CityUpdater.class.getSimpleName();
	
	public CityUpdater(ContextWrapper cw) {
		super(cw);
	}

	public void update() {
		Log.d(TAG, "update City");
		cityUpdate(getCityListFromBase(), getCityListFromServer());
	}
	
	private List<City> getCityListFromBase() {
		List<City> cityBaseList = new ArrayList<City>();
		Cursor cityCursor = mContext.getContentResolver().query(CpCityContract.CITY_CONTENT_URI, null, null, null, null);
		if (cityCursor.moveToFirst()) {
			int i = 0;
			do {
				i++;
				Log.d(TAG, "Запись в базе" + i);
				cityBaseList.add(new City(cityCursor));
			} while (cityCursor.moveToNext());
		}
		cityCursor.close();
		return cityBaseList;
	}

	private List<City> getCityListFromServer() {
		NetHelper a = NetHelper.getInstance(mContext);
		String jsonBody = a.asyncGet(ServerContract.getInstance(mContext).getCity());
		JSONObject jObj = null;
		List<City> cityServerList = new ArrayList<City>();
		try {
			jObj = new JSONObject(jsonBody);
			JSONArray response = jObj.getJSONArray("response");
			for (int i=0; i<response.length(); i++){
				cityServerList.add(new City(response.getJSONObject(i)));
				Log.d(TAG, response.getString(i));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cityServerList;
	}
	
	private City findCityById(City cityToFind, List<City> cityListWhereFind) {
		for (City city : cityListWhereFind){
			if(cityToFind.city_id==city.city_id) return cityToFind;
 		}
		Log.d(TAG, "В искомой коллекции нет city с name=" + cityToFind.name);
		return null;
	}

	private void cityUpdate(List<City> cityBaseList, List<City> cityServerList) {
		ContentValues cv = new ContentValues();
		for (City city : cityServerList){
			City fCity = findCityById(city, cityBaseList);
			// add city if base don't have it
			if (fCity == null) {
				cv.put("name", city.name);
				cv.put("revision", city.revision); 
				cv.put("city_id", city.city_id); 
				cv.put("country", city.country); 
				cv.put("code", city.code); 
				cv.put("phones", city.phones); 
				mContext.getContentResolver().insert(CpCityContract.CITY_CONTENT_URI, cv);
				
			// update city if server have new revision
			}else if(city.revision > fCity.revision){ 
				cv.put("name", city.name);
				cv.put("revision", city.revision); 
				cv.put("city_id", city.city_id); 
				cv.put("country", city.country); 
				cv.put("code", city.code); 
				cv.put("phones", city.phones); 
				Uri updateUri = Uri.parse(CpCityContract.CITY_CONTENT_URI.toString() + "/" + city.city_id);
				mContext.getContentResolver().update(updateUri, cv, null, null);
			}
		}
		
		for (City city : cityBaseList){
			City fCity = findCityById(city, cityServerList);
			// delete city if server don't have it
			if (fCity == null) {
				Uri deleteUri = Uri.parse(CpCityContract.CITY_CONTENT_URI.toString() + "/" + city.city_id);
				mContext.getContentResolver().delete(deleteUri, null, null);
			}
		}
	}

}
