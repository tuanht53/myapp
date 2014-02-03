package com.example.myapp.sync;

import android.content.ContextWrapper;


public class CityEntity extends Entity {

	public CityEntity(ContextWrapper cw){
		mUpdater = new CityUpdater(cw);
	}
}
