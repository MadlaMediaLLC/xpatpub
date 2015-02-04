package com.han.xpatpub.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.han.xpatpub.app.LocationUtility;
import com.han.xpatpub.asynctasks.GeneralAsyncTask;
import com.han.xpatpub.menu.ActionMenu;
import com.han.xpatpub.model.General;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Pub;
import com.han.xpatpub.utility.MyLogUtility;

public class PubParser extends Result {
	
	public static ArrayList<Pub> arrSearchPub = new ArrayList<Pub>();
	public static int nAddPubID;
	
	public static Integer parseSearchPub(String strResponse) {
		try {
			JSONObject jsonObj = new JSONObject(strResponse);
			
			JSONArray arrJsonPub = jsonObj.getJSONArray(General.RECORD);
			ArrayList<Pub> arrPub = new ArrayList<Pub>();
			
			for (int i = 0; i < arrJsonPub.length(); i++) {
				
				JSONObject jsonPub = arrJsonPub.getJSONObject(i);
				Pub pub = new Pub();
				
				pub.id = jsonPub.getInt(Pub.PUB_ID);
				pub.name = jsonPub.getString(Pub.PUB_NAME);
				pub.lat = jsonPub.getDouble(Pub.PUB_LATITUDE);
				pub.lng = jsonPub.getDouble(Pub.PUB_LONGITUDE);
				pub.type = jsonPub.getInt(Pub.PUB_CATEGORY);
				pub.iconUrl = jsonPub.getString(Pub.PUB_ICON);
				pub.dis_min = Double.parseDouble(jsonPub.getString(LocationUtility.DISTANCE_IN_MILES));
				
				JSONObject jsonFeature = jsonPub.getJSONObject("pubfeature");
				
				pub.nVibe = Pub.getHighestNumber(jsonFeature.getString("Vibe"));
				Log.i(PubParser.class.getName(), "pub nVibe = " 
					+ Integer.toString(pub.id) + " : " + Integer.toString(pub.nVibe));
				pub.nTunes = Pub.getHighestNumber(jsonFeature.getString("Tunes"));
				pub.nAles = Pub.getHighestNumber(jsonFeature.getString("AlesonTap"));

				String strFeature = jsonFeature.getString("Action");
				String[] strActions = strFeature.split(",");
				
				for (int j = 0; j < strActions.length; j++) {
					if (strActions[j].equals(Integer.toString(ActionMenu.GAMES_ON_TV))) {
						pub.isGames = true;
					}
					if (strActions[j].equals(Integer.toString(ActionMenu.DARTS))) {
						pub.isDarts = true;
					}
					if (strActions[j].equals(Integer.toString(ActionMenu.POOL))) {
						pub.isPool = true;
					}
					if (strActions[j].equals(Integer.toString(ActionMenu.CARDS))) {
						pub.isCards = true;
					}
					if (strActions[j].equals(Integer.toString(ActionMenu.SHUFFLEBOARD))) {
						pub.isShuffleboard = true;
					}
				}
//				pub.avgRating = jsonObj.getString("avgRating");

//				JSONArray arrJsonRate = jsonPub.getJSONArray("pubRating");
//				
//				pub.arrRate = new ArrayList<Rate>();
//				double avgRate = 0;
//				for (int j = 0; j < arrJsonRate.length(); j++) {
//
//					JSONObject jsonRate = arrJsonRate.getJSONObject(j);
//					
//					Rate rate = new Rate();
//					rate.name = jsonRate.getString("rateCategoryName");
//					rate.avgRate = jsonRate.getDouble("avgRating");
//					avgRate += rate.avgRate;
//					pub.arrRate.add(rate);
//				}
				
//				pub.avgRating = (double) ((double) avgRate / (double) pub.arrRate.size());
				arrPub.add(pub);
			}
			
			arrSearchPub = arrPub;
			
			Log.i(PubParser.class.getName(), "pub count = " + Integer.toString(arrPub.size()));
			return SUCCESS;
			
		} catch (Exception e) {	
			MyLogUtility.error(PubParser.class, e, 1);
			return FAIL;
		}
	}
	
	public static Integer parseCheckBeforeSearchPubs(String strResponse) {
		try {
			JSONObject jsonObj = new JSONObject(strResponse);			
			JSONArray arrJsonPub = jsonObj.getJSONArray(General.RECORD);
			
			if (arrJsonPub.length() <= 0) {
				throw new Exception("Array of pubs is empty.");
			}			
			
		} catch (Exception e) {	
			MyLogUtility.error(PubParser.class, e, 1);
			return FAIL;
		}
		
		return SUCCESS;
	}
	
	public static Integer parseAddPub(String strResponse) {
		try {
			JSONObject jsonObj = new JSONObject(strResponse);
			
			nAddPubID = jsonObj.getInt(Pub.PUB_ID);
			Log.i(PubParser.class.getName(), "add pub id = " + Integer.toString(nAddPubID));
			
			return SUCCESS;
			
		} catch (Exception e) {			
			MyLogUtility.error(PubParser.class, e, 1);
			return FAIL;
		}
	}
	
	public static Integer parsePubFeatureInfo(String strResponse) {
		try {
			JSONObject jsonObj = new JSONObject(strResponse);
			
			JSONArray arrJsonPub = jsonObj.getJSONArray(General.RECORD);
			JSONObject jsonPub = arrJsonPub.getJSONObject(0);

			int pubId = jsonPub.getInt(Pub.PUB_ID);
			Pub pub = GlobalData.getPub(pubId);

			pub.featureCounterId = jsonPub.getInt(Pub.PUB_FEATURE_COUNTER_ID);
			pub.ratingVibeMeetMarket = jsonPub.getInt(Pub.PUB_VIBE_MEET_MARKET);
			pub.ratingVibeLaidback = jsonPub.getInt(Pub.PUB_VIBE_LAIDBACK);
			pub.ratingVibeRaunchy = jsonPub.getInt(Pub.PUB_VIBE_RAUNCHY);
			pub.ratingVibeJazzy = jsonPub.getInt(Pub.PUB_VIBE_JAZZY);
			pub.ratingVibeUpbeat = jsonPub.getInt(Pub.PUB_VIBE_UPBEAT);
			pub.ratingVibeClean = jsonPub.getInt(Pub.PUB_VIBE_CLEAN);
			pub.ratingTunesRock = jsonPub.getInt(Pub.PUB_TUNES_ROCK);
			pub.ratingTunesPop = jsonPub.getInt(Pub.PUB_TUNES_POP);
			pub.ratingTunesRNB = jsonPub.getInt(Pub.PUB_TUNES_RNB);
			pub.ratingTunesCountry = jsonPub.getInt(Pub.PUB_TUNES_COUNTRY);
			pub.ratingTunesMixed = jsonPub.getInt(Pub.PUB_TUNES_MIXED);
			pub.ratingFoodNone = jsonPub.getInt(Pub.PUB_FOOD_NONE);
			pub.ratingFoodVeryGood = jsonPub.getInt(Pub.PUB_FOOD_VERY_GOOD);
			pub.ratingFoodAverage = jsonPub.getInt(Pub.PUB_FOOD_AVERAGE);
			pub.ratingFoodNotGood = jsonPub.getInt(Pub.PUB_FOOD_NOT_GOOD);
			pub.ratingAlesGeneric = jsonPub.getInt(Pub.PUB_ALES_GENERIC);
			pub.ratingAlesLocalCraft = jsonPub.getInt(Pub.PUB_ALES_LOCAL_CRAFT);
			pub.ratingAlesImportCraft = jsonPub.getInt(Pub.PUB_ALES_IMPORT_CRAFT);

			Log.i(PubParser.class.getName(), "pubFeatureCounterID = " + pub.featureCounterId);
			
			return SUCCESS;
			
		} catch (Exception e) {			
			MyLogUtility.error(PubParser.class, e, 1);
			return FAIL;
		}
	}
	
	public static Integer parsePubDetail(String jsonStr) {
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONArray arrJsonPub = jsonObj.getJSONArray(General.RECORD);			
			JSONObject jsonPub = arrJsonPub.getJSONObject(0);
			
			int id = jsonPub.getInt(Pub.PUB_ID);
			Pub pub = GlobalData.getPub(id);
			if (pub == null) {
				pub = new Pub();
			}

			pub.id = jsonPub.getInt(Pub.PUB_ID);
			pub.name = jsonPub.getString(Pub.PUB_NAME);
			pub.type = jsonPub.getInt(Pub.PUB_TYPE);
			pub.lat = jsonPub.getDouble(Pub.PUB_LATITUDE);
			pub.lng = jsonPub.getDouble(Pub.PUB_LONGITUDE);
			pub.country = jsonPub.getInt(Pub.PUB_COUNTRY);
			pub.category = jsonPub.getInt(Pub.PUB_CATEGORY);
			
			pub.isHaveLiveMusic = (jsonPub.getInt(Pub.PUB_HAVE_LIVE_MUSIC) == 1) ? true : false;
			pub.isHaveTVSports = (jsonPub.getInt(Pub.PUB_HAVE_TV_SPORTS) == 1) ? true : false;
			pub.isHavePubGames = (jsonPub.getInt(Pub.PUB_HAVE_POOL_DARTS) == 1) ? true : false;
			
			pub.iconUrl = jsonPub.getString(Pub.PUB_ICON);
			pub.recommendYes = jsonPub.getInt(Pub.PUB_RECOMMEND_YES);
			pub.recommendNo = jsonPub.getInt(Pub.PUB_RECOMMEND_NO);
			pub.totalView = jsonPub.getInt(Pub.PUB_TOTAL_VIEWS);
			pub.founder = jsonPub.getInt(Pub.PUB_FOUNDER);
			pub.owner = jsonPub.getInt(Pub.PUB_OWNER);
			
			if (GlobalData.getPub(pub.id) == null) {
				GlobalData.arrAllPub.add(pub);			
			} 
			
			return Result.SUCCESS;
			
		} catch (Exception e) {			
			MyLogUtility.error(GeneralAsyncTask.class, e, 1);
			return Result.FAIL;
		}
	}
}
