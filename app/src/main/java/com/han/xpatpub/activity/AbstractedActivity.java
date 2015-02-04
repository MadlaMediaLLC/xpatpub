package com.han.xpatpub.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.han.xpatpub.app.App;
import com.han.xpatpub.interfaces.OnBroadcastReceiveInterface;
import com.han.xpatpub.network.Result;

public abstract class AbstractedActivity extends Activity implements
		OnBroadcastReceiveInterface {

	private ResultReceiver resultReceiver;

	public static final String RESULT_RECEIVER_KEY = "receiver";

	@Override
	protected void onResume() {
		super.onResume();
		resultReceiver = new MyResultReceiver(null);
		App.mIntent.putExtra(RESULT_RECEIVER_KEY, resultReceiver);
		App.bindService(getApplication(), false, true);
	}

	@Override
	protected void onPause() {
		App.bindService(getApplication(), true, false);
		super.onPause();
	}

	public class MyResultReceiver extends ResultReceiver {
		public MyResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {

			if (resultCode == Result.SUCCESS) {
				Log.i("AbstractedActivity", "initValue()");
				AbstractedActivity.this.onReceive();
			}
		}
	}
}
