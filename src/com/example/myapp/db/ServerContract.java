package com.example.myapp.db;

public class ServerContract {
	
	public static String COUNTRY = "ua"; 
	
	public static final String BASE_URL = "http://www-api.invitro.";
	public static final String BASE_COLLECTION_URL = BASE_URL + COUNTRY + "/data/1/collection/";
	
	public static final String COLLECTIONS = BASE_URL + COUNTRY + "/data/1/collections";
	public static final String CITY =  BASE_COLLECTION_URL + "city";
	public static final String OFFICE = BASE_COLLECTION_URL + "office";
	public static final String OFFICESERVICE = BASE_COLLECTION_URL + "officeservice";
	public static final String TESTGROUP = BASE_COLLECTION_URL + "testgroup";
	public static final String TEST = BASE_COLLECTION_URL + "test";
	public static final String PRICE = BASE_COLLECTION_URL + "price";

	public static void setCountry(String country){
		COUNTRY = country;
	}
	
}
