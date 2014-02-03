package com.example.myapp.sync;

import android.content.ContextWrapper;

public class RevisionEntity extends Entity {

	public RevisionEntity(ContextWrapper cw) {
		mUpdater = new RevisionUpdater(cw);
	}
}