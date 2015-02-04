package com.han.xpatpub.model;

import java.util.ArrayList;

import com.han.xpatpub.menu.AlesMenuForReview;
import com.han.xpatpub.menu.FoodMenu;
import com.han.xpatpub.menu.TunesMenuForReview;
import com.han.xpatpub.menu.VibeMenu;

public class Pub {

	public static int NONE = -1;
	
	public Pub() {}
	
	public int id;
	public String name;
	public double lat;
	public double lng;
	public double dis_min;
	public int country;
	public int category;
	public int type;
	
	public boolean isHaveLiveMusic;
	public boolean isHaveTVSports;
	public boolean isHavePubGames;
	
	public String iconUrl;
	public int recommendYes;
	public int recommendNo;
	public int totalView;
	public int founder;
	public int owner;
	
	public int nAles;
	public int nFood;
	public int nTunes;
	public int nVibe;
	
	public int featureCounterId;
	public int ratingVibeMeetMarket;
	public int ratingVibeLaidback;
	public int ratingVibeRaunchy;
	public int ratingVibeJazzy;
	public int ratingVibeUpbeat;
	public int ratingVibeClean;
	public int ratingTunesRock;
	public int ratingTunesPop;
	public int ratingTunesRNB;
	public int ratingTunesCountry;
	public int ratingTunesMixed;
	public int ratingFoodNone;
	public int ratingFoodVeryGood;
	public int ratingFoodAverage;
	public int ratingFoodNotGood;
	public int ratingAlesGeneric;
	public int ratingAlesLocalCraft;
	public int ratingAlesImportCraft;
	
	public boolean isGames = false;
	public boolean isDarts = false;
	public boolean isPool = false;
	public boolean isCards = false;
	public boolean isShuffleboard = false;	
	
	public double avgRating;
	
	public ArrayList<Rate> arrRate = new ArrayList<Rate>();
	
	public static final int FEATURE_GROUP_ID_VIBE = 0;
	public static final int FEATURE_GROUP_ID_TUNES = 6;
	public static final int FEATURE_GROUP_ID_ALES = 10;	// TODO change parentID
	public static final int FEATURE_GROUP_ID_FOOD = 15;	// TODO change parentID
	public static final int VOTE_UP = 0;
	public static final int VOTE_DOWN = 1;
			
	public static final String PUB_ID = "pubId";
	public static final String PUB_NAME = "pubName";
	public static final String PUB_LATITUDE = "pubLat";
	public static final String PUB_LONGITUDE = "pubLong";
	public static final String PUB_COUNTRY = "pubCountry";
	public static final String PUB_CATEGORY = "pubCategory";
	public static final String PUB_TYPE = "pubType";
	public static final String PUB_ICON = "pubIcon";
	public static final String PUB_HAVE_LIVE_MUSIC = "pubHaveLiveMusic";
	public static final String PUB_HAVE_TV_SPORTS = "pubHaveTVSports";
	public static final String PUB_HAVE_POOL_DARTS = "pubHavePoolDarts";
	public static final String PUB_RECOMMEND_YES = "pubRecommendYes";
	public static final String PUB_RECOMMEND_NO = "pubRecommendNo";
	public static final String PUB_TOTAL_VIEWS = "pubTotalViews";
	public static final String PUB_FOUNDER = "pubFounder";
	public static final String PUB_OWNER = "pubOwner";
	
	public static final String PUB_FEATURE_ID = "featureId";
	public static final String PUB_FEATURE_COUNTER_ID = "pubFeatureCounterId";
	public static final String PUB_VIBE_MEET_MARKET = "pubVibeMeetMarket";
	public static final String PUB_VIBE_LAIDBACK = "pubVibeLaidback";
	public static final String PUB_VIBE_RAUNCHY = "pubVibeRaunchy";
	public static final String PUB_VIBE_JAZZY = "pubVibeJazzy";
	public static final String PUB_VIBE_UPBEAT = "pubVibeUpbeat";
	public static final String PUB_VIBE_CLEAN = "pubVibeClean";
	public static final String PUB_TUNES_ROCK = "pubTunesRock";
	public static final String PUB_TUNES_POP = "pubTunesPop";
	public static final String PUB_TUNES_RNB = "pubTunesRNB";
	public static final String PUB_TUNES_COUNTRY = "pubTunesCountry";
	public static final String PUB_TUNES_MIXED = "pubTunesMixed";
	public static final String PUB_FOOD_NONE = "pubFoodNone";
	public static final String PUB_FOOD_VERY_GOOD = "pubFoodIsVeryGood";
	public static final String PUB_FOOD_AVERAGE = "pubFoodIsAverage";
	public static final String PUB_FOOD_NOT_GOOD = "pubFoodIsNotGood";
	public static final String PUB_ALES_GENERIC = "pubAlesGeneric";
	public static final String PUB_ALES_LOCAL_CRAFT = "pubAlesLocalCraft";
	public static final String PUB_ALES_IMPORT_CRAFT = "pubAlesImportCraft";
	
	public static String getTypeName(int id) {
		String[] names = {"Western", "Sport", "Irish", "Neighborhood", "Taphouse"};
		
		return names[id - 1];
	}
	
	public static int getTypeIDFromName(String name) {
		String[] names = {"Western", "Sport", "Irish", "Classic", "Taphouse"};
		
		for (int i = 0; i < names.length; i++) {
			if (name.equals(names[i])) {
				return i + 1;
			}
		}
		
		return 1;
	}
	
	public static int getHighestNumber(String strArrNumber) {
		if (strArrNumber.equals("") || strArrNumber == null) return NONE;
		
		String strNumber[] = strArrNumber.split(",");
		int nMax = NONE;
		
		for (int i = 0; i < strNumber.length; i++) {
			int n = Integer.parseInt(strNumber[i]);
			if (n > nMax) nMax = n;
		}
		
		return nMax;
	}
	
	public static String getFeatureKeyById(int id) {
		String result = "";
		
		switch (id) {
		case VibeMenu.MEET_MARKET:
			result = Pub.PUB_VIBE_MEET_MARKET;
			break;
			
		case VibeMenu.LAIDBACK:
			result = Pub.PUB_VIBE_LAIDBACK;
			break;
		
		case VibeMenu.RAUNCHY:
			result = Pub.PUB_VIBE_RAUNCHY;
			break;
		
		case VibeMenu.JAZZY:
			result = Pub.PUB_VIBE_JAZZY;
			break;
			
		// case VibeMenu.UPBEAT:
		// break;
			
		// case VibeMenu.CLEAN:
		// break;
		
		case TunesMenuForReview.ROCK:
			result = Pub.PUB_TUNES_ROCK;
			break;
		
		case TunesMenuForReview.POP:
			result = Pub.PUB_TUNES_POP;
			break;
		
		case TunesMenuForReview.RNB:
			result = Pub.PUB_TUNES_RNB;
			break;
		
		case TunesMenuForReview.COUNTRY:
			result = Pub.PUB_COUNTRY;
			break;
		
		case TunesMenuForReview.MIXED:
			result = Pub.PUB_TUNES_MIXED;
			break;
		
		case FoodMenu.NONE:
			result = Pub.PUB_FOOD_NONE;
			break;
		
		case FoodMenu.VERY_GOOD:
			result = Pub.PUB_FOOD_VERY_GOOD;
			break;
		
		case FoodMenu.AVERAGE:
			result = Pub.PUB_FOOD_AVERAGE;
			break;
		
		case FoodMenu.NOT_GOOD:
			result = Pub.PUB_FOOD_NOT_GOOD;
			break;
		
		case AlesMenuForReview.GENERIC:
			result = Pub.PUB_ALES_GENERIC;
			break;
		
		case AlesMenuForReview.LOCAL:
			result = Pub.PUB_ALES_LOCAL_CRAFT;
			break;
		
		case AlesMenuForReview.IMPORT:
			result = Pub.PUB_ALES_IMPORT_CRAFT;
			break;

		default:
			break;
		}

		return result;
	}
	
	public static int getRatingByFeatureId(Pub curPub, int id) {
		int rating = 0;
		switch (id) {
		case VibeMenu.MEET_MARKET:
			rating = curPub.ratingVibeMeetMarket;
			break;
			
		case VibeMenu.LAIDBACK:
			rating = curPub.ratingVibeLaidback;
			break;
		
		case VibeMenu.RAUNCHY:
			rating = curPub.ratingVibeRaunchy;
			break;
		
		case VibeMenu.JAZZY:
			rating = curPub.ratingVibeJazzy;
			break;
			
		// case VibeMenu.UPBEAT:
		// rating = curPub.ratingVibeUpbeat;
		// break;
			
		// case VibeMenu.CLEAN:
		// rating = curPub.ratingVibeClean;
		// break;
		
		case TunesMenuForReview.ROCK:
			rating = curPub.ratingTunesRock;
			break;
		
		case TunesMenuForReview.POP:
			rating = curPub.ratingTunesPop;
			break;
		
		case TunesMenuForReview.RNB:
			rating = curPub.ratingTunesRNB;
			break;
		
		case TunesMenuForReview.COUNTRY:
			rating = curPub.ratingTunesCountry;
			break;
		
		case TunesMenuForReview.MIXED:
			rating = curPub.ratingTunesMixed;
			break;
		
		case FoodMenu.NONE:
			rating = curPub.ratingFoodNone;
			break;
		
		case FoodMenu.VERY_GOOD:
			rating = curPub.ratingFoodVeryGood;
			break;
		
		case FoodMenu.AVERAGE:
			rating = curPub.ratingFoodAverage;
			break;
		
		case FoodMenu.NOT_GOOD:
			rating = curPub.ratingFoodNotGood;
			break;
		
		case AlesMenuForReview.GENERIC:
			rating = curPub.ratingAlesGeneric;
			break;
		
		case AlesMenuForReview.LOCAL:
			rating = curPub.ratingAlesLocalCraft;
			break;
		
		case AlesMenuForReview.IMPORT:
			rating = curPub.ratingAlesImportCraft;
			break;

		default:
			break;
		}

		return rating;
	}
	
	public static int getRatingByFeatureKey(Pub curPub, String key) {
		int rating = 0;
		
		if (key == PUB_VIBE_MEET_MARKET) {
			rating = curPub.ratingVibeMeetMarket;
					
		} else if (key == PUB_VIBE_LAIDBACK) {
			rating = curPub.ratingVibeLaidback;
			
		} else if (key == PUB_VIBE_RAUNCHY) {
			rating = curPub.ratingVibeRaunchy;
			
		} else if (key == PUB_VIBE_JAZZY) {
			rating = curPub.ratingVibeJazzy;
			
		} else if (key == PUB_VIBE_UPBEAT) {
			rating = curPub.ratingVibeUpbeat;
			
		} else if (key == PUB_VIBE_CLEAN) {
			rating = curPub.ratingVibeClean;
			
		} else if (key == PUB_TUNES_ROCK) {
			rating = curPub.ratingTunesRock;
			
		} else if (key == PUB_TUNES_POP) {
			rating = curPub.ratingTunesPop;
			
		} else if (key == PUB_TUNES_RNB) {
			rating = curPub.ratingTunesRNB;
			
		} else if (key == PUB_TUNES_COUNTRY) {
			rating = curPub.ratingTunesCountry;
			
		} else if (key == PUB_TUNES_MIXED) {
			rating = curPub.ratingTunesMixed;
			
		} else if (key == PUB_FOOD_NONE) {
			rating = curPub.ratingFoodNone;
			
		} else if (key == PUB_FOOD_VERY_GOOD) {
			rating = curPub.ratingFoodVeryGood;
			
		} else if (key == PUB_FOOD_AVERAGE) {
			rating = curPub.ratingFoodAverage;
			
		} else if (key == PUB_FOOD_NOT_GOOD) {
			rating = curPub.ratingFoodNotGood;
			
		} else if (key == PUB_ALES_GENERIC) {
			rating = curPub.ratingAlesGeneric;
			
		} else if (key == PUB_ALES_LOCAL_CRAFT) {
			rating = curPub.ratingAlesLocalCraft;
			
		} else if (key == PUB_ALES_IMPORT_CRAFT) {
			rating = curPub.ratingAlesImportCraft;			
		} 	
		
		return rating;
	}
}
