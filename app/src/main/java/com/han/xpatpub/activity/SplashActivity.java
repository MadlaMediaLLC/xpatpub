package com.han.xpatpub.activity;

import io.fabric.sdk.android.Fabric;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.facebook.Session;
import com.han.xpatpub.R;
import com.han.xpatpub.app.App;
import com.han.xpatpub.interfaces.DialogsListener;
import com.han.xpatpub.utility.DialogSplashUtility;
import com.han.xpatpub.utility.ReleaseUtility;

public class SplashActivity extends Activity implements DialogsListener {
	
	public static final int SUCCESFUL_ACTIVITY_START = 10;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		if (!ReleaseUtility.isRelease(this)) {
			Fabric.with(this, new Crashlytics());
		}
		
		setContentView(R.layout.activity_splash);		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SUCCESFUL_ACTIVITY_START) {
			finish();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();		
		App.mLocationUtility.setCreatingLocationUtility(true);
		App.startLocationUtilityFromActivity(this);	
		new DialogSplashUtility(this).showDialogsIfNeeded();
		
//		boolean isShowInternetDialog = DialogUtility.isShowInternetDialog(this);
//		boolean isShowLocatingDialog = DialogUtility.isShowLocatingDialog(this);
////		boolean isShow00LocationDialog = DialogUtility.isShow00LocationDialog(this);
//		
//		if (isShowInternetDialog) {
//			DialogUtility.displayInternetConnectionFailedDialog(this);
//		}
//
//		if (isShowLocatingDialog) {
//			DialogUtility.displayNetworkLocatingDialog(this);
//		}
//		
////		if (isShow00LocationDialog) {
////			new DialogUtility(this).show00LocationDialogIfNecessary();
////		}
//		
//		if (!isShowInternetDialog && !isShowLocatingDialog) {
////				&& !isShow00LocationDialog) {			
//			continueWithAppFlow();
//		}
	}
	
	@Override
	public void continueWithAppFlow(boolean shouldContinue) {
		if (shouldContinue) {
			if (Session.getActiveSession() != null) {
				startActivityForResult((new Intent(this, ResultActivity.class)), SUCCESFUL_ACTIVITY_START);
	
			} else {
				startActivityForResult(new Intent(this, LoginActivity.class), SUCCESFUL_ACTIVITY_START);
			}
		}
	}
	
	@Override
	public void onPause() {		
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		App.mLocationUtility.setCreatingLocationUtility(false);
		App.stopLocationUtility();
		super.onDestroy();
	}
}
