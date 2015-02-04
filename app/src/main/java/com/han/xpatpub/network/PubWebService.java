package com.han.xpatpub.network;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

import com.han.xpatpub.app.LocationUtility;
import com.han.xpatpub.asynctasks.GeneralAsyncTask;
import com.han.xpatpub.model.General;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Pub;
import com.han.xpatpub.model.Rate;
import com.han.xpatpub.model.URL;
import com.han.xpatpub.utility.MyLogUtility;
import com.han.xpatpub.utility.TimeUtility;

public class PubWebService extends MyGetClient {

	public static Integer searchPub(String strLat, String strLng, String strPubType, String strPubFeature, String strRadius) {
		
		try {			
			String strResponse = getPubs(strLat, strLng, strPubType, strPubFeature, strRadius);			
			return PubParser.parseSearchPub(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(PubWebService.class, e, 0);
	        return Result.FAIL;
		}
	}
	
//	public static Integer checkBeforeSearchForPubs(String strLat, String strLng, String strPubType, String strPubFeature, String strRadius) {
//		
//		try {			
//			String strResponse = getPubs(strLat, strLng, strPubType, strPubFeature, strRadius);			
//			return PubParser.parseCheckBeforeSearchPubs(strResponse);
//			
//		} catch (Exception e) {
//			MyLogUtility.error(PubWebService.class, e, 0);
//	        return Result.FAIL;
//		}
//	}
	
	private static String getPubs(String strLat, String strLng, String strPubType, String strPubFeature, String strRadius) throws Exception {
		String url = URL.URL_CUSTOM + "&action=getNearByPubs" +"&Lat=" + strLat + "&Long=" + strLng + "&session_id=" + GlobalData.currentUser.session_id;
		
		if (!strRadius.equals("")) {
			url += ("&Radius=" + strRadius);
		}
		if (!strPubFeature.equals("")) {
			url += "&pubfeature=" + strPubFeature;
		}
		if (!strPubType.equals("")) {
			url += "&pubType=" + strPubType;
		}
		
		Log.i(PubWebService.class.getName(), "Url = " + url);
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = getClient();
		HttpResponse response = null;

//        httpGet.setEntity(se);
		httpGet = MyGetClient.setHeaders(httpGet);	        
		response = httpClient.execute(httpGet);
		
		String strResponse = EntityUtils.toString(response.getEntity());
		Log.i(PubWebService.class.getName(), "search pub response = " + strResponse);
		
		return strResponse;
	}
	
	public static Integer addPub(String strPubName, String strPubType, String strLat, String strLng, String strPubCountry, 
			String strPubCategory, String strPubIcon, String strPubFounder) {

		try {
			HttpPost httpPost = new HttpPost(URL.URL_PUB);
			HttpClient httpClient = getClient();
			HttpResponse response = null;
			 
            JSONObject jsonObject = new JSONObject();
            
            jsonObject.accumulate(Pub.PUB_NAME, strPubName);
            jsonObject.accumulate(Pub.PUB_TYPE, strPubType);
            jsonObject.accumulate(Pub.PUB_LATITUDE, strLat);
            jsonObject.accumulate(Pub.PUB_LONGITUDE, strLng);
            jsonObject.accumulate(Pub.PUB_COUNTRY, strPubCountry);
            jsonObject.accumulate(Pub.PUB_CATEGORY, strPubCategory);
            jsonObject.accumulate(Pub.PUB_ICON, strPubIcon);
            jsonObject.accumulate(Pub.PUB_FOUNDER, Integer.parseInt(strPubFounder));
            jsonObject.accumulate(General.SESSION_ID, GlobalData.currentUser.session_id);

            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
 
            httpPost.setEntity(se);
            httpPost = MyGetClient.setHeaders(httpPost);            
			response = httpClient.execute(httpPost);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "add pub response = " + strResponse);
			
			return PubParser.parseAddPub(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(PubWebService.class, e, 0);
            return Result.FAIL;
		}
	}
	
	public static Integer addPub(String strName, String strType, String strIsHaveLiveMusic, String strIsHaveTVSports, 
			String strIsHavePubGames) {

		try {
			HttpPost httpPost = new HttpPost(URL.URL_PUB);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
			 
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate(Pub.PUB_NAME, strName);
            jsonObject.accumulate(Pub.PUB_TYPE, strType);
            jsonObject.accumulate(Pub.PUB_LATITUDE, Double.toString(LocationUtility.getCurLat(null)));
            jsonObject.accumulate(Pub.PUB_LONGITUDE, Double.toString(LocationUtility.getCurLng(null)));
            jsonObject.accumulate(Pub.PUB_COUNTRY, GlobalData.countryNumber);
            jsonObject.accumulate(Pub.PUB_CATEGORY, strType);
            jsonObject.accumulate(Pub.PUB_HAVE_LIVE_MUSIC, strIsHaveLiveMusic);
            jsonObject.accumulate(Pub.PUB_HAVE_TV_SPORTS, strIsHaveTVSports);
            jsonObject.accumulate(Pub.PUB_HAVE_POOL_DARTS, strIsHavePubGames);
//            jsonObject.accumulate("pubIcon", "3");
            jsonObject.accumulate(Pub.PUB_FOUNDER, GlobalData.currentUser.userID);
            jsonObject.accumulate(General.SESSION_ID, GlobalData.currentUser.session_id);

            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
 
            httpPost.setEntity(se);
            httpPost = MyGetClient.setHeaders(httpPost);
			response = httpClient.execute(httpPost);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "add pub response = " + strResponse);
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(GeneralAsyncTask.class, e, 0);
            return Result.FAIL;
		}	
	}
	
	public static Integer pubDetail(String strID) {
		try {
			String url = URL.URL_PUB_DETAIL + "&filter=pubId=" + strID;
			
			Log.e("Url = ", url);
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
 
//            httpGet.setEntity(se);
			httpGet = MyGetClient.setHeaders(httpGet);            
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "pub detail response = " + strResponse);
			
			return PubParser.parsePubDetail(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(GeneralAsyncTask.class, e, 0);
            return Result.FAIL;
		}
	}
	
	public static Integer addRate(String strPubID, String[] strRate) {
		try {
			
//			for (int i = 0; i < 5; i++) {
            JSONArray jsonArray = new JSONArray();

			for (int i = 0; i < 5; i++) {
				if (strRate[i].equals("None")) continue;
				
				JSONObject jsonRate = new JSONObject();
				
				jsonRate.put("ratePubId", strPubID);
				jsonRate.put("rateTime", TimeUtility.getCurrentTime(TimeUtility.DATE_FORMAT));
				jsonRate.put("rateOptionsId", i + 1);
				jsonRate.put("rateValues", strRate[i]);
				
				jsonArray.put(jsonRate);
			}
			
			HttpPost httpPost = new HttpPost(URL.URL_ADD_RATE);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
			
			JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate(General.SESSION_ID, GlobalData.currentUser.session_id);
            
            jsonObject.accumulate(General.RECORD, jsonArray);

            String json = jsonObject.toString();
            
            Log.e("Add Rate JSON format", json);
            StringEntity se = new StringEntity(json);
            
            httpPost.setEntity(se);
            httpPost = MyGetClient.setHeaders(httpPost);
			response = httpClient.execute(httpPost);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "add rateresponse = " + strResponse);
			
            return Result.SUCCESS;
            
		} catch (Exception e) {
			MyLogUtility.error(GeneralAsyncTask.class, e, 0);
            return Result.FAIL;
		}	
	}
	
	public static Integer addPubFeature(int pubID, int featureID) {
		try {
			HttpPost httpPost = new HttpPost(URL.URL_PUB_FEATURE);
			HttpClient httpClient = getClient();
			HttpResponse response = null;
			 
            JSONObject jsonObject = new JSONObject();
            
            jsonObject.accumulate(Pub.PUB_ID, Integer.toString(pubID));
            jsonObject.accumulate(Pub.PUB_FEATURE_ID, Integer.toString(featureID));
            jsonObject.accumulate(General.SESSION_ID, GlobalData.currentUser.session_id);

            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
 
            httpPost.setEntity(se);
            httpPost = MyGetClient.setHeaders(httpPost);            
			response = httpClient.execute(httpPost);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "add pub feature response = " + strResponse);
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(PubWebService.class, e, 0);
            return Result.FAIL;
		}
	}
	
	public static Integer deletePubFeaure(int pubID) {
		try {
			String uri = Uri.parse(URL.URL_PUB_FEATURE)
	                .buildUpon()
	                .appendQueryParameter("filter", "pubId=" + Integer.toString(pubID))
	                .build().toString();
			Log.e("delete pub feature Url = ", uri);
			
			HttpDelete httpDelete = new HttpDelete(uri);
			HttpClient httpClient = getClient();
			HttpResponse response = null;
			
			httpDelete = MyGetClient.setHeaders(httpDelete);			
			response = httpClient.execute(httpDelete);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "delete pub feature response = " + strResponse);
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(PubWebService.class, e, 0);
            return Result.FAIL;
		}
	}	
	
	public static Integer getPubInfo(String pubId) {
		try {
			String uri = Uri.parse(URL.URL_PUB)
	                .buildUpon()
	                .appendQueryParameter("filter", "pubId=" + pubId)
	                .build().toString();
			Log.e("pub info Url = ", uri);
			
			HttpGet httpGet = new HttpGet(uri);
			HttpClient httpClient = getClient();
			HttpResponse response = null;
	
	//        httpGet.setEntity(se);
			httpGet = MyGetClient.setHeaders(httpGet);	        
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "pub info response = " + strResponse);
			
			return PubParser.parsePubDetail(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(PubWebService.class, e, 0);
            return Result.FAIL;
		}
	}
	
	public static Integer getPubInfoFromFeatureCounter(String pubId) {
		try {
			String uri = Uri.parse(URL.URL_PUB_RATE)
	                .buildUpon()
	                .appendQueryParameter("filter", "pubId=" + pubId)
	                .build().toString();
			Log.e("pub info from feature Url = ", uri);
			
			HttpGet httpGet = new HttpGet(uri);
			HttpClient httpClient = getClient();
			HttpResponse response = null;
	
	//        httpGet.setEntity(se);
			httpGet = MyGetClient.setHeaders(httpGet);	        
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "pub info from feature response = " + strResponse);
			
			return PubParser.parsePubFeatureInfo(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(PubWebService.class, e, 0);
            return Result.FAIL;
		}
	}
	
	public static Integer addRowInREST(Pub curPub) {
		try {
			HttpPost httpPost = new HttpPost(URL.URL_PUB_RATE);
			HttpClient httpClient = getClient();
			HttpResponse response = null;
			 
            JSONObject jsonObject = new JSONObject();
            
            jsonObject.accumulate(Pub.PUB_ID, Integer.toString(curPub.id));

            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
 
            httpPost.setEntity(se);
            httpPost = MyGetClient.setHeaders(httpPost);            
			response = httpClient.execute(httpPost);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "add pub feature response = " + strResponse);
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(PubWebService.class, e, 0);
            return Result.FAIL;
		}
	}
	
	public static Integer ratePub(Pub curPub, String[] keys) {
		try {
			String uri = Uri.parse(URL.URL_PUB_RATE)
	                .buildUpon()
	                .appendQueryParameter("filter", "pubId=" + curPub.id)
	                .build().toString();
			Log.i("pub rate Url = ", uri);
			
			HttpPatch httpPatch = new HttpPatch(uri);
			HttpClient httpClient = getClient();
			HttpResponse response = null;
			
			JSONObject jsonRecord = new JSONObject();
			for (String key : keys) {				
				jsonRecord.accumulate(key, (Integer) Pub.getRatingByFeatureKey(curPub, key) + 1);
			}

            String json = jsonRecord.toString();
            StringEntity se = new StringEntity(json);
	
            httpPatch.setEntity(se);
			httpPatch = MyGetClient.setHeaders(httpPatch);	        
			response = httpClient.execute(httpPatch);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "pub rate response = " + strResponse);
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(PubWebService.class, e, 0);
            return Result.FAIL;
		}
	}

	public static int vote(Pub pub, String msgVoteType) {
		try {
			String uri = Uri.parse(URL.URL_PUB)
	                .buildUpon()
	                .appendQueryParameter("filter", "pubId=" + pub.id)
	                .build().toString();
			Log.i("pub vote Url = ", uri);
			
			HttpPatch httpPatch = new HttpPatch(uri);
			HttpClient httpClient = getClient();
			HttpResponse response = null;
			
			JSONObject jsonRecord = new JSONObject();
			if (msgVoteType.equals(Integer.toString(Pub.VOTE_UP))) {
				jsonRecord.accumulate(Pub.PUB_RECOMMEND_YES, pub.recommendYes + 1);
				
			} else if (msgVoteType.equals(Integer.toString(Pub.VOTE_DOWN))) {	
				jsonRecord.accumulate(Pub.PUB_RECOMMEND_NO, pub.recommendNo + 1);
			}

            String json = jsonRecord.toString();
            StringEntity se = new StringEntity(json);
	
            httpPatch.setEntity(se);
			httpPatch = MyGetClient.setHeaders(httpPatch);	        
			response = httpClient.execute(httpPatch);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "pub vote response = " + strResponse);
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(PubWebService.class, e, 0);
            return Result.FAIL;
		}
	}
	
	private static Integer search(String strLat, String strLng, String strPubFeature, String strPubType, String strSearchType) {
		
		try {
			String url = URL.URL_CUSTOM + "&action=getNearByPubs" +"&Lat=" + strLat + "&Long=" + strLng + "&session_id=" + GlobalData.currentUser.session_id;
			
			if (strSearchType.equals(GlobalData.SEARCH_TYPE_NONE)) {
				url += "&Radius=" + GlobalData.MILES_FOR_WALK_ONLY;
				
			} else if (strSearchType.equals(GlobalData.SEARCH_TYPE_WALK_ONLY)) {
				url += "&Radius=" + GlobalData.MILES_FOR_WALK_ONLY;
				
			} else if (strSearchType.equals(GlobalData.SEARCH_TYPE_CAB_IT)) {
				url += "&Radius=" + GlobalData.MILES_FOR_CAB_IT;
			}
			
			if (!strPubFeature.equals("")) {
				url += "&pubfeature=" + strPubFeature;
			}
			
			if (!strPubType.equals("")) {
				url += "&pubType=" + strPubType;
			}
			
			Log.e("Url = ", url);
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
 
//            httpGet.setEntity(se);
			httpGet = MyGetClient.setHeaders(httpGet);
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(PubWebService.class.getName(), "search pub response = " + strResponse);
			
			return parseSearch(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(GeneralAsyncTask.class, e, 0);
            return Result.FAIL;
		}
	}

	private static Integer parseSearch(String jsonStr) {		
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			
			JSONArray arrJsonPub = jsonObj.getJSONArray(General.RECORD);
			ArrayList<Pub> arrPub = new ArrayList<Pub>();
			
			for (int i = 0; i < arrJsonPub.length(); i++) {
				
				JSONObject jsonPub = arrJsonPub.getJSONObject(i);
				Pub pub = new Pub();
				
				pub.id = jsonPub.getInt("pubId");
				pub.name = jsonPub.getString("pubName");
				pub.lat = jsonPub.getDouble("pubLat");
				pub.lng = jsonPub.getDouble("pubLong");
				pub.type = jsonPub.getInt("pubCategory");
	//			pub.type = jsonPub.getInt("");
				
				pub.isHaveLiveMusic = (jsonPub.getInt("pubHaveLiveMusic") == 1) ? true : false;
				pub.isHaveTVSports = (jsonPub.getInt("pubHaveTVSports") == 1) ? true : false;
				pub.isHavePubGames = (jsonPub.getInt("pubHavePoolDarts") == 1) ? true : false;
				pub.dis_min = Double.parseDouble(jsonPub.getString("distance_in_mi"));
				
	//			pub.avgRating = jsonObj.getString("avgRating");
	
				JSONArray arrJsonRate = jsonPub.getJSONArray("pubRating");
				
				pub.arrRate = new ArrayList<Rate>();
				double avgRate = 0;
				for (int j = 0; j < arrJsonRate.length(); j++) {
	
					JSONObject jsonRate = arrJsonRate.getJSONObject(j);
					
					Rate rate = new Rate();
					rate.name = jsonRate.getString("rateCategoryName");
					rate.avgRate = jsonRate.getDouble("avgRating");
					avgRate += rate.avgRate;
					pub.arrRate.add(rate);
				}
				
				pub.avgRating = (double) ((double) avgRate / (double) pub.arrRate.size());
				arrPub.add(pub);
			}
			
			GlobalData.arrAllPub = arrPub;
			
			Log.i(GeneralAsyncTask.class.getName(), "pub count = " + Integer.toString(arrPub.size()));
			return Result.SUCCESS;
			
		} catch (Exception e) {			
			MyLogUtility.error(GeneralAsyncTask.class, e, 1);
			return Result.FAIL;
		}
	}
}
