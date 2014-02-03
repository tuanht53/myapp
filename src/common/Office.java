package common;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

public class Office {
	public String name;
	public String metro;
	public String address;
	public int city_id;
	public int revision;
	public int office_id;
	
	public Office(JSONObject o) {
		try {
			name = o.getString("name");
			metro = o.getString("metro");
			address = o.getString("streetaddress");
			city_id = o.getInt("city_id");
			revision = o.getInt("rev");
			office_id = o.getInt("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Office(Cursor cursor) {
		name = cursor.getString(cursor.getColumnIndex("name"));
		metro = cursor.getString(cursor.getColumnIndex("metro"));
		address = cursor.getString(cursor.getColumnIndex("address"));
		city_id = cursor.getInt(cursor.getColumnIndex("city_id"));
		office_id = cursor.getInt(cursor.getColumnIndex("id"));
		revision = cursor.getInt(cursor.getColumnIndex("revision"));
	}
	
}
