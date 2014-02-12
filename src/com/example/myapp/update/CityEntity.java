package com.example.myapp.update;

import android.content.ContextWrapper;


public class CityEntity extends Entity {

	public CityEntity(ContextWrapper cw){
		mUpdater = new CityUpdater(cw);
	}
}
