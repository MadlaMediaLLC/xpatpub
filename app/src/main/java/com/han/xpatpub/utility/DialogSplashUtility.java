package com.han.xpatpub.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;

import com.han.xpatpub.R;
import com.han.xpatpub.activity.SplashActivity;
import com.han.xpatpub.app.App;
import com.han.xpatpub.app.LocationUtility;
import com.han.xpatpub.interfaces.DialogsListener;

public class DialogSplashUtility {
	
	private final String TAG = getClass().getSimpleName();
	
	private SplashActivity activity;

	private boolean mDisplayNetworkLocatingFailedDialog = false;
	private boolean mDisplayInternetConnectionFailedDialog = false;

	private DialogsListener mSplashDialogsListener;

	public DialogSplashUtility(SplashActivity activity) {
		this.activity = activity;
		try {
			mSplashDialogsListener = (DialogsListener) activity;
		
		} catch (Exception e) {
			Log.w(TAG, "Couldn't attach a listener", e);
		}
	}
	
	// TODO do this better
	public void showDialogsIfNeeded() {
		boolean isShowInternetDialog = isShowInternetDialog(activity);
		boolean isShowLocatingDialog = isShowLocatingDialog(activity);
		// boolean isShow00LocationDialog =
		// DialogUtility.isShow00LocationDialog(this);

		if (isShowInternetDialog) {
			displayInternetConnectionFailedDialog();
		}

		if (isShowLocatingDialog) {
			displayNetworkLocatingDialog();
		}

		// if (isShow00LocationDialog) {
		// new DialogUtility(this).show00LocationDialogIfNecessary();
		// }

		if (!isShowInternetDialog && !isShowLocatingDialog) {
			// && !isShow00LocationDialog) {
			continueWithAppFlow();
		}
	}
	
	/**
	 * Dialog showed at the start of application if there is no internet
	 * connection.
	 */
	public void displayInternetConnectionFailedDialog() {
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_connection_failed);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		final CompoundButton check = (CompoundButton) dialog
				.findViewById(R.id.cbDialogConnectionFailedDontShow);
		// if any button is clicked, close the custom dialog
		final Button set = (Button) dialog
				.findViewById(R.id.bTurnONDataTransfer);
		
		// don't erase this, because from Android L and above, setDataEnabled is not accessible
		if (Build.VERSION.SDK_INT >= 21) {
			set.setVisibility(View.GONE);
		}
		
		set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				new AsyncTask<Void, Void, Boolean>() {

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						activity.onPause();
					}

					@Override
					protected Boolean doInBackground(Void... params) {
						if (enableMobileData(activity)) {
							try {
								Thread.sleep(10000);

							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							return true;

						} else {
							return false;
						}
					}

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						if (result) {
							DialogUtility.dismissDialog(dialog);
							activity.onResume();

						} else {
							set.setVisibility(View.GONE);
						}
					}

				}.execute();
			}
		});

		Button configure = (Button) dialog
				.findViewById(R.id.bConfigureSettings);
		configure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
				DialogUtility.dismissDialog(dialog);
				mDisplayInternetConnectionFailedDialog = false;
			}
		});

		Button cancel = (Button) dialog
				.findViewById(R.id.bCancelConFailedDialog);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtility.dismissDialog(dialog);
				mDisplayInternetConnectionFailedDialog = false;
				continueWithAppFlow();
			}
		});

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (check.isChecked()) {
					SharedPrefs prefs = new SharedPrefs(activity);
					prefs.setDisplayConnectionFailed(false);
				}

				mDisplayInternetConnectionFailedDialog = false;
			}
		});

		dialog.setOnKeyListener(new DialogUtility.MyOnKeyListener(dialog));
		DialogUtility.showDialog(dialog);
	}

	/**
	 * Dialog showed after "order accepted" notification is clicked.
	 */
	public void displayNetworkLocatingDialog() {
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_network_locating);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		final CompoundButton check = (CompoundButton) dialog
				.findViewById(R.id.cbDontShowNetworkLocatingDialog);

		Button set = (Button) dialog.findViewById(R.id.bSetNetworkLocating);
		set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.startActivity(new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				DialogUtility.dismissDialog(dialog);
				mDisplayNetworkLocatingFailedDialog = false;
			}
		});

		Button cancel = (Button) dialog
				.findViewById(R.id.bCancelNetworkLocatingDialog);
		// if button is clicked, close the custom dialog
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtility.dismissDialog(dialog);
				mDisplayNetworkLocatingFailedDialog = false;
				continueWithAppFlow();
			}
		});

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				App.mLocationUtility.setCreatingLocationUtility(false);
				if (check.isChecked()) {
					SharedPrefs prefs = new SharedPrefs(activity);
					prefs.setDisplayNetworkLocating(false);
				}

				mDisplayNetworkLocatingFailedDialog = false;
			}
		});

		dialog.setOnKeyListener(new DialogUtility.MyOnKeyListener(dialog));
		
		// if mWelcome || mInternetConnectionFailed is shown, it will wait until
		// they are dismissed
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// while(mWelcome)
				// try {
				// Thread.sleep(100);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }

				while (mDisplayInternetConnectionFailedDialog)
					try {
						Thread.sleep(100);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (mDisplayNetworkLocatingFailedDialog) {
					DialogUtility.showDialog(dialog);
				}
			}			

		}.execute();
	}
		
	public boolean isShowInternetDialog(Context context) {
		SharedPrefs prefs = new SharedPrefs(context);
		mDisplayInternetConnectionFailedDialog = !InternetUtility.isOnline(context)
				&& prefs.getDisplayConnectionFailed();
		return mDisplayInternetConnectionFailedDialog;
	}
	
	public boolean isShowLocatingDialog(Context context) {
		SharedPrefs prefs = new SharedPrefs(context);
		mDisplayNetworkLocatingFailedDialog = (App.mLocationUtility == null || App.mLocationUtility.getCurrentLocation() == null)
				&& (LocationUtility.mLocationManager == null || !LocationUtility.mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
				&& prefs.getDisplayNetworkLocating();
		return mDisplayNetworkLocatingFailedDialog;
	}
	
	private void continueWithAppFlow() {		
		if (!(mDisplayNetworkLocatingFailedDialog 
					|| mDisplayInternetConnectionFailedDialog)) {
			mSplashDialogsListener.continueWithAppFlow(true);			
		} 
	}
	
//	private void continueWithAppFlow(Activity activity) {		
//		if (activity instanceof SplashActivity && 
//				!(mDisplayNetworkLocatingFailedDialog 
//					|| mDisplayInternetConnectionFailedDialog)) {
//			((SplashActivity) activity).continueWithAppFlow();
//			
//		} else if (activity instanceof FeatureActivity) {
//			((FeatureActivity) activity).continueWithAppFlow();
//		}
//	}
	
	/**
	 * Enables mobile data transfer.
	 * 
	 * @param context
	 * 
	 * @return True if enabling is successful
	 */
	private boolean enableMobileData(Context context) {
		ConnectivityManager dataManager;
		dataManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			final Class conmanClass = Class.forName(dataManager.getClass().getName());
		    final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
		    connectivityManagerField.setAccessible(true);
		    final Object connectivityManager = connectivityManagerField.get(dataManager);
		    final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
		    
		    Class[] cArg = new Class[3];
		    cArg[0] = String.class;
		    cArg[1] = Boolean.TYPE;
		    cArg[2] = boolean.class;
		    final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", cArg);
		    
		    setMobileDataEnabledMethod.setAccessible(true);

		    Object[] pArg = new Object[2];
		    pArg[0] = context.getPackageName();
		    pArg[1] = true;
		    setMobileDataEnabledMethod.invoke(connectivityManager, pArg);

		} catch (Exception e) {
			Log.e(TAG, "enableMobileData", e);
			return false;						
		}

		// } catch (SecurityException e) {
		// Log.e(TAG, "Security", e);
		// return false;
		// } catch (NoSuchMethodException e) {
		// Log.e(TAG, "NoSuchMethod", e);
		// return false;
		// } catch (IllegalArgumentException e) {
		// Log.e(TAG, "IllegalArgument", e);
		// return false;
		// } catch (IllegalAccessException e) {
		// Log.e(TAG, "IllegalAccess", e);
		// return false;
		// } catch (InvocationTargetException e) {
		// Log.e(TAG, "InvocationTarget", e);
		// return false;
		// }

		return true;
	}
}
