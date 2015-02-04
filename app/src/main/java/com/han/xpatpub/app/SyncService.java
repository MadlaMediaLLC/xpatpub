package com.han.xpatpub.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		App.updateGlobalDataFromService(getApplicationContext());		
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
