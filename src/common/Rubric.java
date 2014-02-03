package common;

import org.json.JSONObject;

import android.database.Cursor;

public class Rubric{
	public Rubric(){
		
	}
	public Rubric(JSONObject o) {
		sort = o.optInt("sort");
		title = o.optString("title");
		color = (o.optString("color").equals("null")) ? "" : o.optString("color");
		revision = o.optInt("rev");
		parent_id = o.optInt("parent_id");
		id = o.optInt("id");
		String t = o.optString("type");

		if (t.equalsIgnoreCase("TEST")){
			type = TestType.TEST;
		} else if (t.equalsIgnoreCase("PROFILE")) {
			type = TestType.PROFILE;
		} else
			type = TestType.ADDING;
	}
	
	public Rubric(Cursor c){
		id = c.getInt(0);
		color = c.getString(3);
		parentId = c.getInt(6);
		title = c.getString(1);
	}
	
	public TestType type;
	public int parent_id;
	public int revision;
	public int sort;
	public int childCount;
	public int id;
	public String color = "";
	public int parentId;
	public String title = "";
	public int layer;
	
	public int index;
}