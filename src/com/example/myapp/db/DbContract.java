package com.example.myapp.db;

import android.provider.BaseColumns;

public final class DbContract {
	
	public static final String DB_NAME = "database";
	public static final int DB_VERSION = 1;
	
	public static final class RevisionTable implements BaseColumns {

		public static final String REVISION_TABLE_NAME = "revision";

		public static final String COLUMN_NAME_REVISION_NAME = "name";
		public static final String COLUMN_NAME_REVISION_REVISION = "revision";
		
		public static final String CREATE_REVISION_TABLE = "create table "
												+ REVISION_TABLE_NAME + " (" 
												+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
												+ COLUMN_NAME_REVISION_NAME + " TEXT,"
												+ COLUMN_NAME_REVISION_REVISION + " INTEGER"
												+ ");";
		
	}
	
	public static final class CityTable implements BaseColumns {
        
		public static final String CITY_TABLE_NAME = "city";
		
        public static final String COLUMN_NAME_CITY_ID = "city_id";
        public static final String COLUMN_NAME_CITY_NAME = "name";
        public static final String COLUMN_NAME_CITY_COUNTRY = "country";
        public static final String COLUMN_NAME_CITY_CODE = "code";
    	public static final String COLUMN_NAME_CITY_REVISION = "revision";
    	public static final String COLUMN_NAME_CITY_PHONES = "phones";
    	
    	public static final String CREATE_CITY_TABLE = "create table " 
												+ CITY_TABLE_NAME + " ("
												+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
												+ COLUMN_NAME_CITY_ID + " INTEGER,"
												+ COLUMN_NAME_CITY_NAME + " TEXT,"
												+ COLUMN_NAME_CITY_COUNTRY + " CHAR(5),"
												+ COLUMN_NAME_CITY_CODE + " TEXT,"  
												+ COLUMN_NAME_CITY_REVISION + " INTEGER,"  
												+ COLUMN_NAME_CITY_PHONES + " TEXT"
												+ ");"; 
    	
    }
	
	public static final class OfficeTable implements BaseColumns {
        
		public static final String OFFICE_TABLE_NAME = "office";
		
        public static final String COLUMN_NAME_OFFICE_ID = "office_id";
        public static final String COLUMN_NAME_OFFICE_NAME = "name";
        public static final String COLUMN_NAME_OFFICE_METRO = "metro";
        public static final String COLUMN_NAME_OFFICE_CITY_ID = "city_id";
        public static final String COLUMN_NAME_OFFICE_REVISION = "revision";
    	public static final String COLUMN_NAME_OFFICE_COMMENTS = "comments";
    	public static final String COLUMN_NAME_OFFICE_ADDRESS = "address";
    	public static final String COLUMN_NAME_OFFICE_WAY = "way";
    	public static final String COLUMN_NAME_OFFICE_GEO = "geo";
    	public static final String COLUMN_NAME_OFFICE_SERVICELIST = "serviceList";
    	
    	public static final String CREATE_OFFICE_TABLE = "create table " 
												+ OFFICE_TABLE_NAME + " ("
												+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
												+ COLUMN_NAME_OFFICE_ID + " INTEGER,"
												+ COLUMN_NAME_OFFICE_NAME + " TEXT,"
												+ COLUMN_NAME_OFFICE_METRO + " TEXT,"
												+ COLUMN_NAME_OFFICE_CITY_ID + " INTEGER CONSTRAINT [1] REFERENCES city ( id ) ON DELETE SET NULL ON UPDATE SET NULL,"
												+ COLUMN_NAME_OFFICE_REVISION + " INTEGER,"
												+ COLUMN_NAME_OFFICE_COMMENTS + " TEXT,"  
												+ COLUMN_NAME_OFFICE_ADDRESS + " TEXT"
												+ COLUMN_NAME_OFFICE_WAY + " TEXT," 
												+ COLUMN_NAME_OFFICE_GEO + " TEXT," 
												+ COLUMN_NAME_OFFICE_SERVICELIST + " TEXT"
												+ ");"; 
    	
    }

}
