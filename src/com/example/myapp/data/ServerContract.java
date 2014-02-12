package com.example.myapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;


public class ServerContract implements
				OnSharedPreferenceChangeListener {
	
	private static final String TAG = ServerContract.class.getSimpleName();
	
	//public String COUNTRY = "by"; 
	
	public String BASE_URL = "http://www-api.invitro.";
	/*public String BASE_COLLECTION_URL = BASE_URL + COUNTRY + "/data/1/collection/";
	
	public String COLLECTIONS = BASE_URL + COUNTRY + "/data/1/collections";
	public String CITY =  BASE_COLLECTION_URL + "city";
	public String OFFICE = BASE_COLLECTION_URL + "office";
	public String OFFICESERVICE = BASE_COLLECTION_URL + "officeservice";
	public String TESTGROUP = BASE_COLLECTION_URL + "testgroup";
	public String TEST = BASE_COLLECTION_URL + "test";
	public String PRICE = BASE_COLLECTION_URL + "price";*/
	
	private static ServerContract mInstance;
	private Context context;
	private SharedPreferences mPrefs; 
	
	public static ServerContract getInstance(Context context){
		if (mInstance == null) {
			mInstance = new ServerContract(context);
		}
		return mInstance;
	}
	
	private ServerContract(Context ctx){  
	    this.context = ctx;
	    Log.d(TAG, "ServerContract");
	    mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
	    mPrefs.registerOnSharedPreferenceChangeListener(this);
	}  
	
	public String getCollections(){
		return BASE_URL + getCountry() + "/data/1/collections";
	}
	
	public String getCity(){
		return BASE_URL + getCountry() + "/data/1/collection/city";
	}
	
	public String getOffice(){
		return BASE_URL + getCountry() + "/data/1/collection/office";
	}

	private String getCountry() {
		return context.getSharedPreferences("com.example.myapp_preferences", context.MODE_PRIVATE)
    			.getString("pref_country_list", "ru");
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.d(TAG, "ServerOnSharedPreferenceChanged");
	}
	
}
