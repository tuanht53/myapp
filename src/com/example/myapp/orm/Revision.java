package com.example.myapp.orm;

import android.database.Cursor;

public class Revision {
	
	public String name;
	public int revision;
	
	public Revision(String name, int revision) {
		this.name = name;
		this.revision = revision;
		
	}

	public Revision(Cursor c) {
		name = c.getString(c.getColumnIndex("name"));
		revision = c.getInt(c.getColumnIndex("revision"));
	}

}