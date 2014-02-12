package com.example.myapp.orm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;

public class Test {
	public Test(JSONObject o) {
		testId = o.optInt("id");
		sort = o.optInt("sort");
		rubricId = o.optInt("group_id");
		urgent = o.optBoolean("urgent") ? 1 : 0;
		revision = o.optInt("rev");

		title = o.optString("title");
		description = (o.optString("text_opisanie").equals("null")) ? "" : o.optString("text_opisanie");
		interpretation = (o.optString("text_interpetaciya").equals("null")) ? "" : o.optString("text_interpetaciya");
		statement = (o.optString("text_pokazaniya").equals("null")) ? "" : o.optString("text_pokazaniya");
		preparation = (o.optString("text_podgotovka").equals("null")) ? "" : o.optString("text_podgotovka");
		pricecode = (o.optString("pricecode").equals("null")) ? "" : o.optString("pricecode");
		method = (o.optString("text_method").equals("null")) ? "" : o.optString("text_method");
		material = (o.optString("material").equals("null")) ? "" : o.optString("material");

		String t = o.optString("type").toUpperCase();
		type = TestType.TEST;

		if (t.equalsIgnoreCase("TEST"))
			type = TestType.TEST;
		else if (t.equalsIgnoreCase("ADDING"))
			type = TestType.ADDING;
		else
			type = TestType.PROFILE;

		codeList = new ArrayList<String>();
		JSONArray codes = o.optJSONArray("technocodes");

		if (codes != null) {
			for (int i = 0; i < codes.length(); i++) {
				if (codes.optJSONObject(i) != null)
					codeList.add(codes.optJSONObject(i).toString());
			}
		}

		StringBuilder sb = new StringBuilder();
		JSONArray adding = o.optJSONArray("adding_services");
		if (adding != null) {
			for (int i = 0; i < adding.length(); i++) {
				sb.append("," + adding.optString(i).toString());
			}

			serviceList = sb.substring(1);
		}else serviceList = "";
	}

	public Test(Cursor cursor, boolean isExtented) {
		testId = cursor.getInt(0);
		title = cursor.getString(1);
		pricecode = cursor.getString(2);
		this.isExtented = isExtented;
		
		if (cursor.getColumnCount() > 3) {
			description = cursor.getString(2);
			interpretation = cursor.getString(3);
			statement = cursor.getString(4);
			preparation = cursor.getString(5);
			rubricId = cursor.getInt(6);
			pricecode = cursor.getString(7);
			method = cursor.getString(8);
			int t = cursor.getInt(9);

			if (t == 2)
				type = TestType.TEST;
			else if (t == 1)
				type = TestType.ADDING;
			else
				type = TestType.PROFILE;

			material = cursor.getString(10);
			serviceList = cursor.getString(11);

			if (isExtented) {
				currency = cursor.getString(15);
				price = cursor.getInt(16) / 100;
				period = cursor.getInt(17);
			}
		}
	}

	public boolean isExtented;
	public List<String> codeList;

	public String serviceList = "";
	public int testId;
	public TestType type;
	public String title = "";
	public String pricecode = "";
	public String description = "";
	public String preparation = "";
	public String statement = "";
	public String interpretation = "";
	public int rubricId;
	public String method = "";
	public String material = "";

	public String currency = "";
	public double price;
	public int period;

	public int urgent;
	public int revision;
	public int sort;
}