package com.example.myapp.orm;

import org.json.JSONObject;

public class Price {
	public int city_id;
	public int price;
	public int revision;
	public int period;
	public String currency = "";
	public int test_id;

	public Price(JSONObject o) {
		city_id = o.optInt("city_id");
		price = o.optInt("price");
		revision = o.optInt("rev");
		period = o.optInt("period");
		test_id = o.optInt("test_id");
		currency = o.optString("currency");
		price *= 100;
	}
}
