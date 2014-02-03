package common;

import org.json.JSONObject;

public class OfficeService {
	public int id;
	public int revision;
	public int block_id;
	public String name  = "";

	public OfficeService(JSONObject o) {
		id = o.optInt("id");
		revision = o.optInt("rev");
		block_id = o.optInt("block_id");
		name = o.optString("name");
	}
}
