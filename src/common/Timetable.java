package common;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;

public class Timetable {

	public Timetable(Cursor c) {
		this.service_id = c.getInt(9);
		this.description = preProccess(c.getString(1));
		this.monday = preProccess(c.getString(2));
		this.tuesday = preProccess(c.getString(3));
		this.wednesday = preProccess(c.getString(4));
		this.thuesday = preProccess(c.getString(5));
		this.friday = preProccess(c.getString(6));
		this.saturday = preProccess(c.getString(7));
		this.sunday = preProccess(c.getString(8));
		postProccess();
	}
	
	public Timetable(JSONObject object) {
		service_id = object.optInt("service_id");
		description = object.optString("description");
		JSONArray times = object.optJSONArray("time");

		if (times != null) {
			monday = times.optString(0);
			tuesday = times.optString(1);
			wednesday = times.optString(2);
			thuesday = times.optString(3);
			friday = times.optString(4);
			saturday = times.optString(5);
			sunday = times.optString(6);
		}
	}
	
	public Timetable(int id, String description, String monday, String tuesday,
			String wednesday, String thuesday, String friday, String saturday,
			String sunday) {
		this.service_id = id;
		this.description = description;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thuesday = thuesday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		postProccess();
		// }
	}

	private String preProccess(String source){
		return removeTags(source);//source.replace("<br/>", "\n");
	}
	
	private String removeTags(String in)
    {
        int index=0;
        int index2=0;
        while(index!=-1)
        {
            index = in.indexOf("<");
            index2 = in.indexOf(">", index);
            if(index!=-1 && index2!=-1)
            {
                in = in.substring(0, index).concat(in.substring(index2+1, in.length()));
            }
        }
        return in;
    }
	
	private void postProccess() {
		if (monday.length() == 0)
			monday = "-";
		if (tuesday.length() == 0)
			tuesday = "-";
		if (wednesday.length() == 0)
			wednesday = "-";
		if (thuesday.length() == 0)
			thuesday = "-";
		if (friday.length() == 0)
			friday = "-";
		if (saturday.length() == 0)
			saturday = "-";
		if (sunday.length() == 0)
			sunday = "-";
	}

	public int service_id;
	public String description = "";
	public String monday = "";
	public String tuesday = "";
	public String wednesday = "";
	public String thuesday = "";
	public String friday = "";
	public String saturday = "";
	public String sunday = "";

	public String get(int index) {
		switch (index) {
		case 0:
			return monday;
		case 1:
			return tuesday;
		case 2:
			return wednesday;
		case 3:
			return thuesday;
		case 4:
			return friday;
		case 5:
			return saturday;
		case 6:
			return sunday;
		default:
			return "";
		}
	}
}
