package com.han.xpatpub.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.han.xpatpub.model.General;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.User;
import com.han.xpatpub.utility.MyLogUtility;

public class PatronParser extends Result {

//	public static ArrayList<User> arrSearchUser = new ArrayList<User>();
	
	public static Integer parseSearchUser(String strResponse) {
		
		try {
			JSONObject jsonObj = new JSONObject(strResponse);
			
			JSONArray arrJsonUser = jsonObj.getJSONArray(General.RECORD);
			ArrayList<User> arrUser = new ArrayList<User>();
			
			for (int i = 0; i < arrJsonUser.length(); i++) {				
				JSONObject jsonUser = arrJsonUser.getJSONObject(i);
				User user = new User();
				
				user.userID 		= jsonUser.getString(User.USER_ID);
				user.userName 		= jsonUser.getString(User.USER_NAME);
				user.userEmail 			= jsonUser.getString(User.USER_EMAIL);
				user.userLat 		= jsonUser.getString(User.USER_LATITUDE);
				user.userLng 		= jsonUser.getString(User.USER_LONGITUDE);
				user.userLastLogin 	= jsonUser.getString(User.USER_LAST_LOGIN);
				user.userPrivacy 	= jsonUser.getString(User.USER_PRIVACY);
				user.userType 		= jsonUser.getString(User.USER_TYPE);

				arrUser.add(user);
			}
			
			GlobalData.arrSearchUser = arrUser;
			
			Log.d(PatronParser.class.getName(), "user count = " + Integer.toString(arrUser.size()));
			return SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(PatronParser.class, e, 1);
			return FAIL;
		}
	}
}
