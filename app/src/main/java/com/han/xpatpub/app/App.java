package com.han.xpatpub.app;

import java.util.Calendar;

import org.apache.http.Header;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.Session;
import com.han.xpatpub.activity.AbstractedActivity;
import com.han.xpatpub.activity.AbstractedActivity.MyResultReceiver;
import com.han.xpatpub.activity.LoginActivity;
import com.han.xpatpub.asynctasks.FileAsyncTask;
import com.han.xpatpub.asynctasks.GeneralAsyncTask;
import com.han.xpatpub.asynctasks.UserAsyncTask;
import com.han.xpatpub.interfaces.AddPubListener;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.network.Result;
import com.han.xpatpub.utility.SharedPrefs;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

public class App extends Application {	   
	
	private static AlarmManager mAlarm = null;
	private static MyResultReceiver mResultReceiver = null;
	private static PendingIntent pIntent = null;
	
	private static final int ALARM_PERIOD_IN_MILLISECONDS = 40000; 
	
	public static Activity currentActivity = null;
	public static App application;
	
	public static AddPubListener mAddPubListener = null;	
	public static boolean resultActivitySet = false;
	
	public static Intent mIntent;
	
	public static LocationUtility mLocationUtility = null;
//	public static DialogUtility mDialogUtility = null;	
	
	@Override
	public void onCreate() {		
		application = this;
		SharedPrefs prefs = new SharedPrefs(this);
		prefs.setDisplay00Location(true);
		mLocationUtility = new LocationUtility(application);
		mLocationUtility.setCreatingLocationUtility(true);
		mIntent = new Intent(this, SyncService.class);
		bindService(this, false, true);
	}

	@Override
	public void onTerminate() {
		Log.e("App", "onTerminate");		
		LoginActivity.fb_logout();
		mLocationUtility.onStopLocationService();
		stopService(mIntent);
		mLocationUtility.setCurrentLocation(null);
		mLocationUtility = null;
		SharedPrefs prefs = new SharedPrefs(this);
		prefs.setDisplay00Location(false);
		super.onTerminate();
	}
	
	/**
	 * TODO Optimize this one.
	 * 
	 * @param calledFromService TODO
	 * @param activity TODO
	 * 
	 */
	public static void updateGlobalData(Context context, boolean calledFromService, Activity activity) {
		if(GlobalData.currentUser.userEmail == null && activity != null) {
			resultActivitySet = false;
			Log.i("App", "session_id = " + GlobalData.currentUser.session_id);
			((App) activity.getApplication()).session();
			LoginActivity.fb_login(activity);
		}
		
		Log.i("App.updateGlobalData()", "updating");
		if (calledFromService) {
			new UserAsyncTask(context).execute(Action.ACTION_UPDATE_USER_LOCATION);
			new UserAsyncTask(context).execute(Action.ACTION_USER_PUB);
			new GeneralAsyncTask(context).execute(Action.ACTION_USER_MESSAGE);		
			
			if (GlobalData.currentUser.userType.equals("2")) {
				new GeneralAsyncTask(context).execute(Action.ACTION_PUB_OWNER_DETAIL);
				
				String strLat = Double.toString(LocationUtility.getCurLat(activity));
				String strLng = Double.toString(LocationUtility.getCurLng(activity));
				String strRadius = "500";
				
				new FileAsyncTask(context).execute(Action.FILE_COUPON_LIST);
				new GeneralAsyncTask(context).execute(Action.ACTION_USER_LIST, strLat, strLng, strRadius);
			}
			
		} else {
			new UserAsyncTask(activity).execute(Action.ACTION_UPDATE_USER_LOCATION);
			new UserAsyncTask(activity).execute(Action.ACTION_USER_PUB);
			new GeneralAsyncTask(activity).execute(Action.ACTION_USER_MESSAGE);		
			
			if (GlobalData.currentUser.userType.equals("2")) {
				new GeneralAsyncTask(activity).execute(Action.ACTION_PUB_OWNER_DETAIL);
				
				String strLat = Double.toString(LocationUtility.getCurLat(activity));
				String strLng = Double.toString(LocationUtility.getCurLng(activity));
				String strRadius = "500";
				
				new FileAsyncTask(activity).execute(Action.FILE_COUPON_LIST);
				new GeneralAsyncTask(activity).execute(Action.ACTION_USER_LIST, strLat, strLng, strRadius);
			}
		}
	}
	
	public static void bindService(Application context, boolean fromActivity, boolean bind) {
		unbindService(context);		
		
		if (bind) {
			bindToService(context);
			
		} else {
			// this sets the alarm after finishing Activity which used receiver with Service
			if (fromActivity) {
				mIntent.removeExtra(AbstractedActivity.RESULT_RECEIVER_KEY);
				bindToService(context);
			}
		}				
	}
	
	private static void bindToService(Application context) {
		pIntent = PendingIntent.getService(context, 0, mIntent, 0);
		
		mAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		// Start every 30 seconds
		// InexactRepeating allows Android to optimize the energy consumption
		mAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				Calendar.getInstance().getTimeInMillis(), 
				ALARM_PERIOD_IN_MILLISECONDS, 
				pIntent);
//		alarm.setRepeating(AlarmManager.RTC_WAKEUP, 
//				Calendar.getInstance().getTimeInMillis(), 
//				ALARM_PERIOD_IN_MILLISECONDS, 
//				pIntent);
	}
	
	private static void unbindService(Application application) {
		if (mAlarm != null) {
			mAlarm.cancel(pIntent);
			application.stopService(mIntent);
		}		
	}
	
	public static void updateGlobalDataFromService(Context context) {
		if (Session.getActiveSession() != null) {			
			updateGlobalData(context, true, null);
		}
		
		mResultReceiver = mIntent.getParcelableExtra(AbstractedActivity.RESULT_RECEIVER_KEY);
		
		if (mResultReceiver != null) {
			mResultReceiver.send(Result.SUCCESS, null);
		}
	}
	
	public void session() {
		mLocationUtility.onCreateLocationService();
		// TODO change password here
		new GeneralAsyncTask(getApplicationContext()).execute(Action.ACTION_SESSION, "yogesh.m@lucidsolutions.in", "123456");	
		
//		mLocationUtility.onStopLocationService();
	}	
	
	// TODO Use this method!
	private void getBraintreeClientToken() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("https://your-server/client_token",	// TODO add our server information
				new TextHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						
					}
				});
	}
	
	public static void setAddPubListener(Activity activity) {
		mAddPubListener = (AddPubListener) activity;
	}
	
	public static void startLocationUtilityFromActivity(Activity activity) {
		App.setActivity(activity);
		App.mLocationUtility.onCreateLocationService();	
	}
	
	public static void stopLocationUtility() {
		if (mLocationUtility != null) {
			mLocationUtility.onStopLocationService();
		}
	}
	
	public static void setActivity(Activity activity) {
		currentActivity = activity;
		
		if (activity != null) {			
			if (mLocationUtility == null) {
				mLocationUtility = new LocationUtility(activity);			
			}
			
			mLocationUtility.setActivity(activity);
		}
	}
}
