package com.han.xpatpub.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class User {
	
	public String firstName;
	public String lastName;
	public String session_id;
	public String userCode;
	public String userCoupons;
	public String userCritic = "1";
	public String userEmail;
	public String userFounder = "";
	public String userID;
	public String userName;
	public String userLastLogin;
	public String userLat;
	public String userLng;
	public String userPrivacy = "1";
	public String userSpecial;
	public String userStart;
	public String userStatus;
	public String userToken;
	public String userType = "1";
    public String userClientToken;
	
	public static final String USER_CODE = "userCode";
	public static final String USER_COUPON = "userCoupon";
	public static final String USER_CRITIC = "userCritic";
	public static final String USER_EMAIL = "userEmail";
	public static final String USER_FOUNDER = "userFounder";
	public static final String USER_ID = "userId";
	public static final String USER_LAST_LOGIN = "userLastLogin";
	public static final String USER_LATITUDE = "userLat";
	public static final String USER_LONGITUDE = "userLong";
	public static final String USER_NAME = "userName";
	public static final String USER_PRIVACY = "userPrivacy";
	public static final String USER_SPECIAL = "userSpecial";
	public static final String USER_START = "userStart";
	public static final String USER_STATUS = "userStatus";
	
	public static final String USER_TOKEN = "userToken";
	public static final String USER_TYPE = "userType";	
	
	public static final int USER_PRIVACY_PRIVATE = 1;
	public static final int USER_PRIVACY_ANONYMOUS = 2;
	public static final int USER_PRIVACY_PUBLIC = 3;
	
	public static final int USER_TYPE_PATRON = 1;
	public static final int USER_TYPE_OWNER = 2;
	
	public static String getLabelPubType(String strType) {		
		String result = "";
		
		if (strType.equals(String.valueOf(USER_TYPE_PATRON))) {
			result = "Pub Patron";
			
		} else if (strType.equals(String.valueOf(USER_TYPE_OWNER))) {
			result = "Pub Owner";
		}
		
		return result;
	}
	
	private static void putSessionIdInSharedPrefs(Context context, String session_id) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), 0);
		Editor editor = prefs.edit();
		editor.putString(General.SESSION_ID, session_id).commit();
	}
}