package com.example.myapp.sync;

import android.content.ContextWrapper;

public abstract class Updater {
	ContextWrapper mContext;
	
	public Updater(ContextWrapper cw){
		mContext = cw;
	}
	
	public abstract void update();
}
