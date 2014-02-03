package common;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

public class City {
	
	public int sort;
	public int city_id;
	public String country;
	public String code;
	public String name;
	public int revision;
	public String phones;
	
	public City(JSONObject o) {
		try {
			city_id = o.getInt("id");
			country = o.getString("country");
			code = o.getString("code");
			name = o.getString("name");
			revision = o.getInt("rev");
			phones = o.getString("phones");
			sort = 3;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public City(Cursor cursor) {
		city_id = cursor.getInt(cursor.getColumnIndex("city_id"));
		name = cursor.getString(cursor.getColumnIndex("name"));
		country = cursor.getString(cursor.getColumnIndex("country"));
		code = cursor.getString(cursor.getColumnIndex("code"));
		revision = cursor.getInt(cursor.getColumnIndex("revision"));
		phones = cursor.getString(cursor.getColumnIndex("phones"));
	}
}