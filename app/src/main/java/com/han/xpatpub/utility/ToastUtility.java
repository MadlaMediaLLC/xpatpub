package com.han.xpatpub.utility;

import android.app.Activity;
import android.widget.Toast;

public class ToastUtility {

	private static final int SLEEP_TIME = 10;
	
	public static void showCantSearchPubs(Activity activity, boolean delay) {
		Toast toast = Toast.makeText(activity, "Due to unavailable address, you can't search for pubs", Toast.LENGTH_SHORT);
		
		if (delay) {
	    	delayToastMessage(toast);
	    	
	    } else {
	    	toast.show();
	    }
	}
	
	private static void showLocationServiceDisabled(Activity activity, boolean delay) {
		Toast toast = Toast.makeText(activity, "Location service is disabled. Please, enable it to enhance your experience!", Toast.LENGTH_SHORT);
		
	    if (delay) {
	    	delayToastMessage(toast);
	    	
	    } else {
	    	toast.show();
	    }
	}
	
	public static void showGPSEnabled(Activity activity) {
		Toast.makeText(activity, "GPS Disconnected.", Toast.LENGTH_SHORT).show();
	}
	
	private static void delayToastMessage(final Toast toast) {
		Thread thread = new Thread( new Runnable() {

			@Override
	        public void run() {
	            try {
	                Thread.sleep(SLEEP_TIME);
	                
	            } catch (InterruptedException e) {}
	            
	            toast.show();
	        }
	    });
		
	    thread.start();
	}
}
