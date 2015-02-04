package com.han.xpatpub.utility;

import android.util.Log;

public class MyLogUtility {
	
	private static final String MSG_CLIENT_PROTOCOL_EXCEPTION = "ClientProtocolException";
	private static final String MSG_JSON_PARSING_EXCEPTION = "json parsing error";
	
	/**
	 * Prints message like this: action + "\n" + response
	 * 
	 * @param class1
	 * @param action
	 * @param response
	 */
	public static void info(Class<?> class1, String action, String response) {
		MyLogUtility.info(class1.getName(), action, response);	
	}
	
	/**
	 * Prints message like this: action + "\n" + response
	 * 
	 * @param className
	 * @param action
	 * @param response
	 */
	public static void info(String className, String action, String response) {
		Log.i(className, action + "\n" + response);
	}
	
	/**
	 * Prints error message with class name as tag and "ClientProtocolException" message header
	 * @param class1
	 * @param e
	 * @param message 0 - "ClientProtocolException", 1 - "json parsing error", other - ""
	 */
	public static void error(Class<?> class1, Exception e, int message) {
		MyLogUtility.error(class1.getName(), e, message);		
	}

	/**
	 * Prints error message with class name as tag and "ClientProtocolException" message header
	 * @param e
	 * @param message 0 - "ClientProtocolException", 1 - "json parsing error", other - ""
	 * @param class1 Pass something like this UserWebService.class.getName()
	 */
	public static void error(String className, Throwable e, int message) {
		switch (message) {
		case 0:
			Log.e(className, MyLogUtility.MSG_CLIENT_PROTOCOL_EXCEPTION, e);
			break;

		case 1:
			Log.e(className, MyLogUtility.MSG_JSON_PARSING_EXCEPTION, e);
			break;
			
		default:
			Log.e(className, "", e);
			break;
		}		
	}	
}
