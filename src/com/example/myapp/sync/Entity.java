package com.example.myapp.sync;



public abstract class Entity {
	Updater mUpdater;
	
	public void performUpdate(){
		mUpdater.update();
	}
	
	public void setUpdater(Updater upd){
		mUpdater = upd;
	}
}
