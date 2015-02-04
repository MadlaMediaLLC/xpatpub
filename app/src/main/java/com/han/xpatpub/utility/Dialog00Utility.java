package com.han.xpatpub.utility;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;

import com.han.xpatpub.R;
import com.han.xpatpub.adapters.PlacesAutoCompleteAdapter;
import com.han.xpatpub.app.App;
import com.han.xpatpub.app.LocationUtility;
import com.han.xpatpub.interfaces.DialogsListener;
import com.han.xpatpub.interfaces.OnShow00LocationDialogListener;
import com.han.xpatpub.utility.DialogUtility.MyOnKeyListener;

public class Dialog00Utility {

	private static final String TAG = "DialogActivity";
	private static boolean shouldDisplay = false;	
	private static boolean isDisplayed = false;
	private static Dialog dialog00 = null;
	private static double latitude = 0;
	private static double longitude = 0;	
	private static DialogsListener mDialogsListener;
	
//	public Dialog00Utility(Activity activity) {
//		this.activity = activity;
//		setDialogsListener(acitivity);
//	}

	/**
	 * 
	 * @param activity If null, dialog will not be shown.
	 */
	public static boolean isShouldShow00Dialog(Activity activity) {
		boolean result = false;
		if (activity != null) {
			try {
	    		OnShow00LocationDialogListener dialogListener = (OnShow00LocationDialogListener) activity;
				if (dialogListener != null) {					
//					mDialogUtility.setActivity(activity);
//					Dialog00Utility.show00LocationDialogIfNecessary(activity);
					if (isShow00LocationDialog(activity)) {	
						result = true;
						run00LocationDialogOnMainThread(activity);								
					} 
				}
				
	    	} catch (Exception e) {
	    		Log.w("DialogUtility", "Activity doesn't have a listener", e);
	    	}
		}
		
		return result;
	}
	
	private static void setDialogsListener(Activity activity) {
		try {
			mDialogsListener = (DialogsListener) activity;
			
		} catch (Exception e) {
			Log.w(TAG, "Couldn't connect listener to activity", e);
		}
	}
	
	
	// TODO Add handler
	private static void run00LocationDialogOnMainThread(final Activity activity) {
		if (shouldDisplay && !isDisplayed) {		
			isDisplayed = true;
			activity.runOnUiThread(new Runnable() {
		    	
		    	@Override
		        public void run() {  		
		    		show00LocationDialog(activity);
		        }
		    });
		}
	}
	
	private static void show00LocationDialog(final Activity activity) {
		dialog00 = new Dialog(activity);
		dialog00.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog00.setContentView(R.layout.dialog_0_0_location);
		dialog00.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		final CompoundButton check = (CompoundButton) dialog00
				.findViewById(R.id.cbDontShow);
		
		PlacesAutoCompleteAdapter autoComAdapter = new PlacesAutoCompleteAdapter(activity, R.layout.row_autocomplete);;
		final AutoCompleteTextView autoCompleteText = (AutoCompleteTextView) dialog00
				.findViewById(R.id.actvAddress);
		autoCompleteText.setAdapter(autoComAdapter);
		autoCompleteText.setOnItemClickListener(new OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
//	        	setDepartureLocationOnMap(check.getText().toString());
	        	String address = autoCompleteText.getText().toString();
	        	List<Address> addresses = null;

				try {
					//trying to get all possible addresses by search pattern
					Geocoder geocoder = new Geocoder(activity);
					addresses = geocoder.getFromLocationName(address, 3);
					
				} catch (IOException e) {
					
				} catch (Exception e) {
//					Toast.makeText(activity, "Please, try again! Something went wrong with picking an address...", Toast.LENGTH_SHORT).show();
				}

				if (addresses == null) {
					// location service unavailable or incorrect address so returns null
					return;
				}

				Address closest = null;
				float closestDistance = Float.MAX_VALUE;
				// look for address, closest to our location
				for (Address adr : addresses) {
					if (closest == null) {
						closest = adr;
						
					} else {
						float[] result = new float[1];
						Location loc = App.mLocationUtility.getCurrentLocation();
						Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), 
								adr.getLatitude(), adr.getLongitude(), result);
						float distance = result[0];
						if (distance < closestDistance) {
							closest = adr;
							closestDistance = distance;
						}
					}
				}

				if (closest != null) {
					latitude = closest.getLatitude();
					longitude = closest.getLongitude();
//					App.setLatLonFromLocation(null);	// LEAVE null as the parameter, because it breaks infinite loop!!!
//					double lat = closest.getLatitude();  
//					double lng = closest.getLongitude();  
//					LatLng latLng = new LatLng(lat, lng);
//					if (map != null) {		
//						changeMapPosition = false;	// prevents onCameraChange from current change
//						map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//					} 
				}
			}	        
	    });
		
		// if any button is clicked, close the custom dialog
		final Button set = (Button) dialog00
				.findViewById(R.id.bSet);
		
		set.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO set Lat/Lng from address
				Location loc = App.mLocationUtility.getCurrentLocation();
				if (loc == null) {
					loc = new Location("dummyProvider");
				}
				
				loc.setLatitude(latitude);
				loc.setLongitude(longitude);
				App.mLocationUtility.setCurrentLocation(loc);
				LocationUtility.setLatLonFromLocation(null);

				DialogUtility.dismissDialog(dialog00);
				shouldDisplay = false;
				isDisplayed = false;
				
				continueWithAppFlow(activity, true);
			}			
		});
		
		Button cancel = (Button) dialog00
				.findViewById(R.id.bCancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtility.dismissDialog(dialog00);				
				shouldDisplay = false;
				isDisplayed = false;
				continueWithAppFlow(activity, false);
			}
		});

		dialog00.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (check.isChecked()) {
					SharedPrefs prefs = new SharedPrefs(activity);
					prefs.setDisplay00Location(false);
				}

				shouldDisplay = false;
				isDisplayed = false;
			}
		});
		
		dialog00.setOnKeyListener(new MyOnKeyListener(dialog00));
		DialogUtility.showDialog(dialog00);	
	}
	
	private static void continueWithAppFlow(Activity activity, boolean shouldContinue) {
		try {
			mDialogsListener = (DialogsListener) activity;
			if (mDialogsListener != null) {
				mDialogsListener.continueWithAppFlow(shouldContinue);
			}
			
		} catch (Exception e) {}		
	}
	
	private static boolean isShow00LocationDialog(Activity activity) {
		if (activity != null) {
			if (App.mLocationUtility != null
					&& App.mLocationUtility.getCurrentLocation() != null) {

				String strLat = Double.toString(App.mLocationUtility
						.getCurrentLocation().getLatitude());
				String strLng = Double.toString(App.mLocationUtility
						.getCurrentLocation().getLongitude());

				if (strLat.equals("0.0") && strLng.equals("0.0")) {
					shouldDisplay = true;
				}				
				
			} else if (!App.mLocationUtility.isCreatingLocationUtility()) {
				SharedPrefs prefs = new SharedPrefs(activity);
				if (prefs.getDisplay00Location()) {
					shouldDisplay = true;				
				}
			}
			
		} else {
			// TODO Add handler to show this dialog when next activity is active
			shouldDisplay = false;
		}
		
		return shouldDisplay;
	}
	
//	private static void show00LocationDialogIfNecessary(Activity activity) {
//		if (isShow00LocationDialog(activity)) {			
//			if (dialog00 != null
//					&& (!dialog00.isShowing() || (dialog00.getOwnerActivity() != activity))) {
//
//				DialogUtility.dismissDialog(dialog00);
//				dialog00 = null;
//			}
//			
//			if (dialog00 == null) {
//				run00LocationDialogOnMainThread(activity);
////				showDialog();
//			}
//		}
//	}
	
	private static void showWithDialogListener(Activity activity) {
    	try {
    		OnShow00LocationDialogListener dialogListener = (OnShow00LocationDialogListener) activity;
			if (dialogListener != null) {
				dialogListener.show00LocationDialog();
			}
			
    	} catch (Exception e) {
    		Log.w("DialogUtility", "Activity doesn't have a listener", e);
    	}
    }
}
