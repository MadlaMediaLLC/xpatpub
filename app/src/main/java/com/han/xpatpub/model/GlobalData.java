package com.han.xpatpub.model;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;

import com.han.xpatpub.app.App;

public class GlobalData {
	
	public static User currentUser = new User();
	
	public static double MILES_FOR_METER_10			= 0.0062137;
	public static double MILES_FOR_WALK_ONLY 		= 0.4;
	public static double MILES_FOR_CAB_IT	 		= 3.3;

	public static String SEARCH_TYPE_NONE			= "none";
	public static String SEARCH_TYPE_WALK_ONLY 		= "walk_only";
	public static String SEARCH_TYPE_CAB_IT 		= "cab_it";
	
	public static String isoCountryCode;
	public static String countryNumber;
	
	public static ArrayList<Pub> arrAllPub 			= new ArrayList<Pub>();
	public static ArrayList<Pub> arrMyPub 			= new ArrayList<Pub>();
	public static ArrayList<Message> arrMyMessage 	= new ArrayList<Message>();
	public static ArrayList<Coupon> arrMyCoupon 	= new ArrayList<Coupon>();
	public static ArrayList<Advertise> arrAdvertise = new ArrayList<Advertise>();
	public static ArrayList<User> arrSearchUser 	= new ArrayList<User>();
	
	public static Pub ownPub = new Pub();
	
	public static String curCityName = "";
	
	public static Bitmap bmpProfile;		
		
	public static double getRadius(String searchType) {
		if (searchType.equals(SEARCH_TYPE_WALK_ONLY)) {
			return MILES_FOR_WALK_ONLY;
		}
		
		if (searchType.equals(SEARCH_TYPE_CAB_IT)) {
			return MILES_FOR_CAB_IT;
		}
		
		return MILES_FOR_WALK_ONLY;
	}
	
	public static Pub getPub(int pubID) {		
		for (int i = 0; i < arrAllPub.size(); i++) {
			Pub pub = arrAllPub.get(i);
			if (pub.id == pubID) {
				return pub;
			}
		}
		
		return null;
	}
	
	public static int getPubPosition(int pubId) {
		for (int i = 0; i < arrAllPub.size(); i++) {
			Pub pub = arrAllPub.get(i);
			if (pub.id == pubId) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Use this in InboxActivity and PubPatronActivity.
	 * 
	 * @return
	 */
	public static int getUsabaleCouponsNumber() {
		int i = 0;
		for (Message message : arrMyMessage) {
			if (message.msgStatus == Message.MESSAGE_NOT_USED) {
				i++;
			}
		}
		return i;
	}
	
	/**
	 * Use this in InboxActivity.
	 * 
	 * @return Part of {@link #GlobalData()#arrMyMessage} list.
	 */
	public static ArrayList<Message> getUsableCoupons() {
		ArrayList<Message> arrData = new ArrayList<Message>();
		for (Message message : GlobalData.arrMyMessage) {
			if (message.msgStatus == 0) {
				arrData.add(message);
			}
		}
		
		return arrData;
	}
	
	/**
	 * Use this in SearchPatronActivity.
	 * 
	 * @return Part of {@link #GlobalData()#arrSearchUser} list.
	 */
	public static ArrayList<User> getPatrons() {
		ArrayList<User> arrData = new ArrayList<User>();
		for (User user : GlobalData.arrSearchUser) {
			if (Integer.valueOf(user.userType) == User.USER_TYPE_PATRON) {
				arrData.add(user);
			}
		}
		
		return arrData;
	}
	
	public static void clearGlobalData() {
		currentUser = new User();
		arrAllPub = new ArrayList<Pub>();
		arrMyPub = new ArrayList<Pub>();
		arrMyMessage = new ArrayList<Message>();
		arrMyCoupon = new ArrayList<Coupon>();
		arrAdvertise = new ArrayList<Advertise>();
		arrSearchUser = new ArrayList<User>();		
		ownPub = new Pub();		
		curCityName = "";		
		bmpProfile = null;
	}
}
