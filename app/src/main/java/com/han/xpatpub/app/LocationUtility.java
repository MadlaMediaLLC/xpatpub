package com.han.xpatpub.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.han.xpatpub.R;
import com.han.xpatpub.activity.FeatureActivity;
import com.han.xpatpub.asynctasks.PubAsyncTask;
import com.han.xpatpub.interfaces.OnShow00LocationDialogListener;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.utility.Dialog00Utility;
import com.han.xpatpub.utility.ToastUtility;

public class LocationUtility implements LocationListener, 
				ConnectionCallbacks, OnConnectionFailedListener {

	private Activity activity;
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    
	private static boolean isCreatingLocationUtility = false;
	
	private static final String TAG = "LocationUtility";
	
	// Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private static boolean mResolvingError = false;
    
	private static Location mCurrentLocation = null;
	private static double curLat = 0.0;
	private static double curLng = 0.0;
	
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //** Update interval in milliseconds */
    public static final int LOCATION_UPDATE_INTERVAL_IN_MILLISECONDS = 3000;
    //** A fast ceiling of update intervals, used when the app is visible */
    public static final int LOCATION_UPDATE_INTERVAL_FAST_IN_MILLISECONDS = 500;
    
    public static final float MINIMUM_UPDATE_DISTANCE_IN_METERS = 0.2f;
    
    public static OnShow00LocationDialogListener dialogListener;
    
    // Create an empty string for initializing strings
    public static final String EMPTY_STRING = new String();
    public static final String DISTANCE_IN_MILES = "distance_in_mi";
    	
	// Stores the current instantiation of the location client in this object
//    public static FusedLocationProviderApi mLocationClient = null;    
	public static LocationManager mLocationManager = null;	
//	private static Criteria criteria = null;
    
    /**
     * This is used only in the App.java
     * @param context
     */
    LocationUtility(Context context) {
    	this.activity = null;
    	this.context = context;
    }
    
    void setActivity(Activity activity) {
    	this.activity = activity;
    	this.context = activity;
    }
    
    public void setContext(Context context) {
    	this.context = context;
    }
    
    public Location getCurrentLocation() {
		return mCurrentLocation;
	}
	
	public void setCurrentLocation(Location location) {		
		mCurrentLocation = location;
		curLat = mCurrentLocation.getLatitude();
		curLng = mCurrentLocation.getLongitude();
		Log.i(LocationUtility.class.getName(), "Current location is set:\n" + "Latitude = " + curLat + "\n" + "Longitude = " + curLng);
	}
	
	public static double getCurLat(Activity activity) {
//		Dialog00Utility.show00Dialog(activity);
//		Dialog00Utility.showWithDialogListener(activity);

		Log.d(LocationUtility.class.getName(), "Getting Latitude = " + curLat);
		return curLat;
	}

	public static double getCurLng(Activity activity) {
//		Dialog00Utility.show00Dialog(activity);
//		Dialog00Utility.showWithDialogListener(activity);

		Log.d(LocationUtility.class.getName(), "Getting Longitude = " + curLng);
		return curLng;
	}
	
	public boolean isCreatingLocationUtility() {
		return isCreatingLocationUtility;
	}

	public void setCreatingLocationUtility(boolean isCreatingLocationUtility) {
		LocationUtility.isCreatingLocationUtility = isCreatingLocationUtility;
	}
    
    protected synchronized void buildGoogleApiClient() {
    	// Create a GoogleApiClient instance
        mGoogleApiClient = new GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
    }
    
	public LocationRequest initLocationRequest(final int requestCode) {
//		buildGoogleApiClient();

		LocationRequest locationRequest = LocationRequest.create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(LocationUtility.LOCATION_UPDATE_INTERVAL_IN_MILLISECONDS)
				.setFastestInterval(LocationUtility.LOCATION_UPDATE_INTERVAL_FAST_IN_MILLISECONDS);

		PendingResult<Status> result = LocationServices.FusedLocationApi
				.requestLocationUpdates(mGoogleApiClient, // your connected
															// GoogleApiClient
						locationRequest, // a request to receive a new location
						this); // the listener which will receive updated locations

		// Callback is asynchronous. Use await() on a background thread or
		// listen for
		// the ResultCallback
		result.setResultCallback(new ResultCallback<Status>() {
			
			public void onResult(Status status) {
				if (status.isSuccess()) {
					// Successfully registered
				} else if (status.hasResolution()) {
					// Google provides a way to fix the issue
					try {
						// your current activity used to receive the result
						// the result code you'll look for in your
						// onActivityResult method to retry registering
						status.startResolutionForResult(activity, requestCode);
						
					} catch (SendIntentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					// No recovery. Weep softly or inform the user.
					Log.e(TAG, "Registering failed: " + status.getStatusMessage());
				}
			}
		});

		return locationRequest;
		
//		LocationRequest request = LocationRequest.create();
//
//		/*
//		 * Set the update interval
//		 */
//		request.setInterval(LocationUtility.LOCATION_UPDATE_INTERVAL_IN_MILLISECONDS);
//
//		// Use high accuracy
//		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//		return request.setFastestInterval(LocationUtility.LOCATION_UPDATE_INTERVAL_FAST_IN_MILLISECONDS);
    }			

	/**
	 * 
	 * @param activity
	 */
	public void openLocationInActivityAndSearchForPubs() {		
		onCreateLocationService();
		
		String strLat = Double.toString(getCurLat(activity));
		String strLng = Double.toString(getCurLng(activity));		
		
		if (!(strLat.equals("0.0") && strLng.equals("0.0"))) {
			new PubAsyncTask(activity).execute(Action.ACTION_SEARCH_PUB, strLat, strLng, "", 
					FeatureActivity.strFeature, Double.toString(GlobalData.MILES_FOR_CAB_IT));
		
		} else {
			((FeatureActivity) activity).setButtonEnabled(true);
			ToastUtility.showCantSearchPubs(activity, true);
		}
	}

	public void onCreateLocationService() {
//		setAddPubListener(activity);
		onStartLocationService();
		locationForSession();
	}
	
	/**
	 * Connects the LocationClient
	 */
	public void onStartLocationService() { 			
		if (mGoogleApiClient == null) {
//			initLocationRequest(0);	
			buildGoogleApiClient();
		}
		
		if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
			mGoogleApiClient.connect();			
		}
		
//		initLocationRequest(0);
	}

	/**
	 * Called just once in the App.java
	 */
	public void locationForSession() {
		if (mLocationManager == null && activity != null) {
			mLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		}
		
		// TODO add this later
//		if (mLocationManager == null 
//				|| !(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) 
//						|| mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
//			
////			ToastUtility.showLocationServiceDisabled(activity, true);
//		}		
		
//		
//        // Creating a criteria object to retrieve provider
//		if (criteria == null) {
//			criteria = new Criteria();
//		}
//
//        // Getting the name of the best provider
//        String provider = mLocationManager.getBestProvider(criteria, true);
//
//        // Getting Current Location
//        Location location = mLocationManager.getLastKnownLocation(provider);
////        setLatLonFromLocation();		
//        if(location != null) {
//            onLocationChanged(location);
//        }
		
		if (mGoogleApiClient.isConnected()) {
			getLastLocation();
	        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, 
	        		initLocationRequest(0), this);
		}
	}
	
	/**
	 * Disconnects LocationClient and removes updates from LocationManager
	 */
	void onStopLocationService() {
		if (mGoogleApiClient != null) {
			mGoogleApiClient.disconnect();
		}
		
//		if (mLocationManager != null) {
//			mLocationManager.removeUpdates(this);
//        	mLocationManager = null;
//		}
		
		App.currentActivity = null;
	}
	
	/**
	 * NOTE: I think this method is unnecessary...
	 * 
	 * This is the only method where Lat/Lng is set.
	 * @param activity
	 */
	public static void setLatLonFromLocation(Activity activity) {
		try {
//			App.show00Dialog(activity);
			
			String strLat = null;
			String strLng = null;
			
			if (mCurrentLocation != null) {
				strLat = Double.toString(LocationUtility.getCurLat(activity));
				strLng = Double.toString(LocationUtility.getCurLng(activity));
			}

			if ((strLat != null && strLng != null && strLat.equals("0.0") && strLng.equals("0.0"))
					|| mCurrentLocation == null && activity != null) {
				
				LocationUtility lu = new LocationUtility(activity);
				lu.onStopLocationService();
				lu.onCreateLocationService();
				setLatLonFromLocation(null); // DON'T REMOVE THIS. It breaks the
												// infinite loop.
			}
				
//				if (!(strLat.equals("0.0") && strLng.equals("0.0"))) {
//				    setCurLat(mCurrentLocation.getLatitude());
//				    setCurLng(mCurrentLocation.getLongitude());
////				    Log.i(App.class.getName(), "Latitude = " + strLat + "\nLongitude = " + strLng);		
//				    
//				} else if (activity != null) {
//					elseIf(activity);	
//				} 
			
		} catch (Exception e) {
			Log.e(LocationUtility.class.getName(), "setLatLonFromLocation", e);
		}
    }
	
	// The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(Activity activity, int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment(activity);
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        // TODO dialogFragment.show(activity.getFragmentManager(), "errordialog");
    }

	@Override
	public void onLocationChanged(Location location) {
//		Dialog00Utility.showWithDialogListener(activity);
		Dialog00Utility.isShouldShow00Dialog(activity);
		
		if (location == null) {
			return;
			
		} else {
			setCurrentLocation(location);
		}
		
		setLatLonFromLocation(activity);
		
		if (App.mAddPubListener != null) {
			App.mAddPubListener.firstOnLocationChange();
		}
		
//		if (activity instanceof FeatureActivity) {
//			((FeatureActivity) activity).continueWithAppFlow();
//		}
	    
//	    locationManager.removeUpdates(this);		
	}

	/*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
            
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(App.currentActivity, REQUEST_RESOLVE_ERROR);
                
            } catch (SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
            
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(App.currentActivity, connectionResult.getErrorCode());
            mResolvingError = true;
        }
		
		/*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                		App.currentActivity,
                        LocationUtility.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */                
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                Log.e("App.java", "IntentSender.SendIntentException", e);
            }
            
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
//            showDialog(connectionResult.getErrorCode());
        }
	}
	
	@Override
	public void onConnected(Bundle arg0) {
		getLastLocation();
		
//		initLocationRequest(0);
		
		// Display the connection status
		Log.d(LocationUtility.class.getName(), "GPS Connected");
//        Toast.makeText(context, "GPS Connected", Toast.LENGTH_SHORT).show();
	}
	
	private void getLastLocation() {
		Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
		onLocationChanged(mLastLocation);
	}

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
	@Override
	public void onConnectionSuspended(int arg0) {
		// Display the connection status
        ToastUtility.showGPSEnabled(activity);
	}
	
	/** A fragment to display an error dialog */
    private static class ErrorDialogFragment extends DialogFragment {
    	
        public ErrorDialogFragment(Activity activity) {
//        	mActivity = activity;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
        	onDialogDismissed();
        }
    }
    
    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    private static void onDialogDismissed() {
        mResolvingError = false;
    }    
    
    /**
     * Get the latitude and longitude from the Location object returned by
     * Location Services.
     *
     * @param currentLocation A Location object containing the current location
     * @return The latitude and longitude of the current location, or null if no
     * location is available.
     */
    public static String getLatLng(Context context, Location currentLocation) {
        // If the location is valid
        if (currentLocation != null) {
            // Return the latitude and longitude as strings
            return context.getString(
                    R.string.latitude_longitude,
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude());
            
        } else {
            // Otherwise, return the empty string
            return EMPTY_STRING;
        }
    }
}
