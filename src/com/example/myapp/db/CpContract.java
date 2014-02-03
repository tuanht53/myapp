package com.example.myapp.db;

import com.example.myapp.db.DbContract.CityTable;
import com.example.myapp.db.DbContract.OfficeTable;
import com.example.myapp.db.DbContract.RevisionTable;

import android.net.Uri;
import android.provider.BaseColumns;

public final class CpContract {

	public static final String AUTHORITY = "com.example.myapp.DbContentProvider";
	
	public static final class CpRevisionContract {

		public static final String REVISION_PATH = RevisionTable.REVISION_TABLE_NAME;

		public static final Uri REVISION_CONTENT_URI = Uri.parse("content://"
					+ AUTHORITY + "/" + REVISION_PATH);

	}
	
	public static final class CpCityContract {
        
		public static final String CITY_PATH = CityTable.CITY_TABLE_NAME;
		
        public static final Uri CITY_CONTENT_URI = Uri.parse("content://"
        			+ AUTHORITY + "/" + CITY_PATH);
       
    }
	
	public static final class CpOfficeContract implements BaseColumns {
        
		public static final String OFFICE_PATH = OfficeTable.OFFICE_TABLE_NAME;
		
		public static final Uri OFFICE_CONTENT_URI = Uri.parse("content://"
			       + AUTHORITY + "/" + OFFICE_PATH);
    	
    }
}
