package com.han.xpatpub.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefs {
	private SharedPreferences pref;
	private Editor editor;
	
	public SharedPrefs(Context context) {
		pref = context.getSharedPreferences(MY_PREFS, 0); // 0 - for private mode
		editor = pref.edit();
	}
	
	/**
	 * Taxi Centrala shared preferences.
	 * 
	 * @see SharedPrefs#DISPLAY_WELCOME_DIALOG
	 * @see SharedPrefs#DISPLAY_VALIDATION_DIALOG
	 * @see SharedPrefs#DISPLAY_SEND_ORDER_DIALOG
	 * @see SharedPrefs#MAIN_ACTIVITY_RUNNING
	 * @see SharedPrefs#CAN_ORDER
	 * @see SharedPrefs#USER_VERIFIED
	 * @see SharedPrefs#DEV_MODE
	 */
	public static final String MY_PREFS = "com.han.xpatpub";
	
	/** * Key for showing internet connection failed dialog. */
	private static final String DISPLAY_CONNECTION_FAILED_DIALOG = "dialogconnectionfailed";
	
	/** Key for showing location over network dialog. */
	private static final String DISPLAY_NETWORK_LOCATING_DIALOG = "dialoglocationovernetwork";
	
	/** Key for showing 00 location dialog. */
	private static final String DISPLAY_00_LOCATING_DIALOG = "dialog00location";
	


	
	/**
	 * Sets {@link SharedPrefs#DISPLAY_CONNECTION_FAILED_DIALOG} to value. 
	 * If true, dialog will be shown.
	 */
	public void setDisplayConnectionFailed(boolean value) {
		editor.putBoolean(DISPLAY_CONNECTION_FAILED_DIALOG, value).commit();
	}

	/**
	 * Retrieve a boolean value from the {@link SharedPrefs#MY_PREFS}.
	 * 
	 * @return Returns retrieved boolean value of {@link SharedPrefs#DISPLAY_CONNECTION_FAILED_DIALOG}
	 * or true if it doesn't exist.
	 */
	public boolean getDisplayConnectionFailed() {
		return pref.getBoolean(DISPLAY_CONNECTION_FAILED_DIALOG, true);
	}
	
	/**
	 * Sets {@link SharedPrefs#DISPLAY_NETWORK_LOCATING_DIALOG} to value. 
	 * If true, dialog will be shown.
	 */
	public void setDisplayNetworkLocating(boolean value) {
		editor.putBoolean(DISPLAY_NETWORK_LOCATING_DIALOG, value).commit();
	}

	/**
	 * Retrieve a boolean value from the {@link SharedPrefs#MY_PREFS}.
	 * 
	 * @return Returns retrieved boolean value of {@link SharedPrefs#DISPLAY_NETWORK_LOCATING_DIALOG}
	 * or true if it doesn't exist.
	 */
	public boolean getDisplayNetworkLocating() {
		return pref.getBoolean(DISPLAY_NETWORK_LOCATING_DIALOG, true);
	}
	
	/**
	 * Sets {@link SharedPrefs#DISPLAY_NETWORK_LOCATING_DIALOG} to value. 
	 * If true, dialog will be shown.
	 */
	public void setDisplay00Location(boolean value) {
		editor.putBoolean(DISPLAY_00_LOCATING_DIALOG, value).commit();
	}

	/**
	 * Retrieve a boolean value from the {@link SharedPrefs#MY_PREFS}.
	 * 
	 * @return Returns retrieved boolean value of {@link SharedPrefs#DISPLAY_NETWORK_LOCATING_DIALOG}
	 * or true if it doesn't exist.
	 */
	public boolean getDisplay00Location() {
		return pref.getBoolean(DISPLAY_00_LOCATING_DIALOG, true);
	}
}
