package com.han.xpatpub.asynctasks;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.han.xpatpub.activity.LoginActivity;
import com.han.xpatpub.app.App;
import com.han.xpatpub.app.LocationUtility;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.Coupon;
import com.han.xpatpub.model.General;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Pub;
import com.han.xpatpub.model.URL;
import com.han.xpatpub.model.User;
import com.han.xpatpub.network.MessageParser;
import com.han.xpatpub.network.MessageWebService;
import com.han.xpatpub.network.MyGetClient;
import com.han.xpatpub.network.Result;
import com.han.xpatpub.utility.MyLogUtility;
import com.han.xpatpub.utility.MyToastUtility;
import com.han.xpatpub.utility.TimeUtility;

public class UserAsyncTask extends AbstractedAsyncTask {
	
	public UserAsyncTask(Activity activity) {
		super(activity);
	}
	
	public UserAsyncTask(Context context) {
		super(context);		
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		nResult = Result.FAIL;
		
		curAction = params[0];
		
		if (curAction.equals(Action.ACTION_LOGIN)) {
			nResult = login(params[1]);
			
		} else if (curAction.equals(Action.ACTION_REGISTER)) {
			String userName = params[1];
			String userToken = params[2];
			String userEmail = params[3];
			String userType = params[4];
			String userCritic = params[5];
			String userFounder = params[6];
			nResult = register(userName, userToken, userEmail, userType, userCritic, userFounder);
			
		} else if (curAction.equals(Action.ACTION_USER_PUB)) {
			nResult = readMyPubList();
			
		} else if (curAction.equals(Action.ACTION_UPDATE_USER_PRIVACY)) {
			String strPrivacy = params[1];
			nResult = updateUserPrivacy(strPrivacy);
			
		} else if (curAction.equals(Action.ACTION_UPDATE_USER_LOCATION)) {
			
			nResult = updateUserLocation();
		} else if (curAction.equals(Action.OWNER_SEND_COUPON)) {
			String msgText = "test msg1";
			String msgStatus = "0";
			String msgSenderId = params[1];
			String msgReceiverId = params[2];
			String msgPubId = params[3];
			String msgCouponId = params[4];
			
			nResult = MessageWebService.addMessage(msgText, msgStatus, msgSenderId, msgReceiverId, msgPubId, msgCouponId);
			
			if (nResult == Result.SUCCESS) {
//				nResult = sendCoupon(msgCouponId, --Coupon.couponUsesLimit);				
//				if (nResult == Parser.SUCCESS) {
					nResult = readMyMessage(msgCouponId);
//				}
			}
		}
		
		return nResult;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		
		if (curAction.equals(Action.ACTION_LOGIN)) {
			if (nResult == Result.SUCCESS) {
				LoginActivity.successLogin(parent);	
				if (activity instanceof LoginActivity) {
					((LoginActivity) activity).setButtonEnabled(true);
				}
				
			} else if (nResult == Result.FAIL) {
				LoginActivity.register(parent);
			}
			
		} else if (curAction.equals(Action.ACTION_REGISTER)) {
			if (nResult == Result.SUCCESS) {
				
				App.application.session();
				LoginActivity.reLogin(parent);
				
			} else if (nResult == Result.FAIL) {
				if (activity instanceof LoginActivity) {
					((LoginActivity) activity).setButtonEnabled(true);
					MyToastUtility.showToast(parent, MyToastUtility.MESSAGE_REGISTERING_UNSUCCESSFUL, true);
				}
			}
			
		} else if (curAction.equals(Action.ACTION_USER_PUB)) {
			
		} else if (curAction.equals(Action.ACTION_UPDATE_USER_LOCATION)) {
			
		} else if (curAction.equals(Action.OWNER_SEND_COUPON)) {
			if (nResult == Result.SUCCESS) {
				Toast.makeText(parent, "Coupon sent successufly", Toast.LENGTH_LONG).show();
				
			} else if (nResult == Result.FAIL) {
				Toast.makeText(parent, "Coupon is not sent successufly", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public static Integer login(String strUserName) {
		try {
//			String url = URL_LOGIN + "&filter=username='" + strUserName + "'";
			String uri = Uri.parse(URL.URL_LOGIN)
	                .buildUpon()
	                .appendQueryParameter("filter", "username='" + strUserName + "'")
	                .build().toString();
			Log.i(UserAsyncTask.class.getName(), "GET login Url = " + uri);
			HttpGet httpGet = new HttpGet(uri);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
 
//            httpGet.setEntity(se);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("X-DreamFactory-Session-Token", GlobalData.currentUser.session_id);
            
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(UserAsyncTask.class.getName(), "Login response = " + strResponse);
			
			Integer integer = parseLogin(strResponse);
			
			return integer;
			
		} catch (Exception e) {
            Log.e(UserAsyncTask.class.getName(), "ClientProtocolException", e);
            return Result.FAIL;
		}
	}
	
	public static Integer parseLogin(String jsonStr) {
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);			
			JSONArray arrJsonUser = jsonObj.getJSONArray(General.RECORD);
			if (arrJsonUser.length() == 0) {
				return Result.FAIL;
			}
			
			JSONObject jsonUser = arrJsonUser.getJSONObject(0);

			GlobalData.currentUser.userCode 	= jsonUser.getString(User.USER_CODE);
			GlobalData.currentUser.userCoupons 	= jsonUser.getString(User.USER_COUPON);
			GlobalData.currentUser.userCritic 	= jsonUser.getString(User.USER_CRITIC);
			GlobalData.currentUser.userEmail 	= jsonUser.getString(User.USER_EMAIL);
			GlobalData.currentUser.userFounder 	= jsonUser.getString(User.USER_FOUNDER);
			GlobalData.currentUser.userID 		= jsonUser.getString(User.USER_ID);
			GlobalData.currentUser.userLat 		= jsonUser.getString(User.USER_LATITUDE);
			GlobalData.currentUser.userLng 		= jsonUser.getString(User.USER_LONGITUDE);
			GlobalData.currentUser.userName 	= jsonUser.getString(User.USER_NAME);
			GlobalData.currentUser.userSpecial 	= jsonUser.getString(User.USER_SPECIAL);
			GlobalData.currentUser.userStart 	= jsonUser.getString(User.USER_START);
			GlobalData.currentUser.userStatus 	= jsonUser.getString(User.USER_STATUS);			
			GlobalData.currentUser.userToken	= jsonUser.getString(User.USER_TOKEN);
			GlobalData.currentUser.userType 	= jsonUser.getString(User.USER_TYPE);

			Log.i(UserAsyncTask.class.getName(), "Current user ID = " + GlobalData.currentUser.userID);
			return Result.SUCCESS;
			
		} catch (Exception e) {			
			MyLogUtility.error(UserAsyncTask.class, e, 1);
			return Result.FAIL;
		}
	}

	public static Integer register(String userName, String userToken, String userEmail, String userType, 
			String userCritic, String userFounder) {
		
		try {
			HttpPost httpPost = new HttpPost(URL.URL_REGISTER);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
			
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate(User.USER_NAME, userName);
            jsonObject.accumulate(User.USER_TOKEN, userToken);
            jsonObject.accumulate(User.USER_EMAIL, userEmail);
            jsonObject.accumulate(User.USER_TYPE, userType);
            jsonObject.accumulate(User.USER_LATITUDE, Double.toString(LocationUtility.getCurLat(null)));
            jsonObject.accumulate(User.USER_LONGITUDE, Double.toString(LocationUtility.getCurLng(null)));
            jsonObject.accumulate(User.USER_CRITIC, userCritic);
            jsonObject.accumulate(User.USER_PRIVACY, User.USER_PRIVACY_ANONYMOUS);
            jsonObject.accumulate(User.USER_FOUNDER, 1);
            jsonObject.accumulate(User.USER_LAST_LOGIN, TimeUtility.getCurrentTime(TimeUtility.DATE_FORMAT));
            jsonObject.accumulate(General.SESSION_ID, GlobalData.currentUser.session_id);

            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
 
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("X-DreamFactory-Session-Token", GlobalData.currentUser.session_id);
            
			response = httpClient.execute(httpPost);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(UserAsyncTask.class.getName(), "register response = " + strResponse);
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(UserAsyncTask.class, e, 0);
            return Result.FAIL;
		}
	}
	
	public static Integer readMyPubList() {
		try {
//			String url = URL_LOGIN + "&filter=username='" + strUserName + "'";
			String uri = Uri.parse(URL.URL_PUB_DETAIL)
	                .buildUpon()
	                .appendQueryParameter("filter", "pubFounder=" + GlobalData.currentUser.userID)
	                .build().toString();
			
			Log.i(UserAsyncTask.class.getName(), "GET MY pub Data Url = " + uri);
			HttpGet httpGet = new HttpGet(uri);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
 
//            httpGet.setEntity(se);
			MyGetClient.setHeaders(httpGet);            
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(UserAsyncTask.class.getName(), "readMyPubList() response = " + strResponse);		
			return parseMyPubs(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(UserAsyncTask.class, e, 0);
            return Result.FAIL;
		}
	}
	
	public static Integer parseMyPubs(String jsonStr) {		
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			
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
				pub.country = jsonPub.getInt(Pub.PUB_COUNTRY);
				pub.iconUrl = jsonPub.getString(Pub.PUB_ICON);
//				pub.type = jsonPub.getInt("");
				
				pub.isHaveLiveMusic = (jsonPub.getInt(Pub.PUB_HAVE_LIVE_MUSIC) == 1) ? true : false;
				pub.isHaveTVSports = (jsonPub.getInt(Pub.PUB_HAVE_TV_SPORTS) == 1) ? true : false;
				pub.isHavePubGames = (jsonPub.getInt(Pub.PUB_HAVE_POOL_DARTS) == 1) ? true : false;
//				pub.dis_min = Double.parseDouble(jsonPub.getString("distance_in_mi"));
				
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
			
			GlobalData.arrMyPub = arrPub;
			
			Log.i(UserAsyncTask.class.getName(), "my pub count = " + Integer.toString(arrPub.size()));
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(UserAsyncTask.class, e, 1);
			return Result.FAIL;
		}
	}
	
	public static Integer updateUserPrivacy(String strPrivacy) {
		try {
			HttpPatch httpPatch = new HttpPatch(URL.URL_UPDATE_USER_PRIVACY);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
			
			JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("userId", GlobalData.currentUser.userID);
            jsonObject.accumulate("userPrivacy", strPrivacy);
            jsonObject.accumulate("session_id", GlobalData.currentUser.session_id);

            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
 
            httpPatch.setEntity(se);
            httpPatch.setHeader("Accept", "application/json");
            httpPatch.setHeader("Content-type", "application/json");
            httpPatch.setHeader("X-DreamFactory-Session-Token", GlobalData.currentUser.session_id);
            
			response = httpClient.execute(httpPatch);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(UserAsyncTask.class.getName(), "updateUserPrivacy() response = " + strResponse);		
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(UserAsyncTask.class, e, 0);
			return Result.FAIL;
		}
	}
	
	private static Integer updateUserLocation() {		
		try {
			HttpPatch httpPatch = new HttpPatch(URL.URL_UPDATE_USER_LOCATION);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
			
			JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("userId", GlobalData.currentUser.userID);
            jsonObject.accumulate("userLat", LocationUtility.getCurLat(null));
            jsonObject.accumulate("userLong", LocationUtility.getCurLng(null));
            jsonObject.accumulate("userLastLogin", TimeUtility.getCurrentTime(TimeUtility.DATE_FORMAT));
            
            JSONArray arrJsonRecord = new JSONArray();
            arrJsonRecord.put(jsonObject);
            
            JSONObject jsonRecord = new JSONObject();
            jsonRecord.accumulate("record", arrJsonRecord);
            
            String json = jsonRecord.toString();
            StringEntity se = new StringEntity(json);
 
            httpPatch.setEntity(se);
            httpPatch = MyGetClient.setHeaders(httpPatch);  
            
			response = httpClient.execute(httpPatch);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(UserAsyncTask.class.getName(), "updateUserLocation() response = " + strResponse);
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(UserAsyncTask.class, e, 0);
			return Result.FAIL;
		}
	}
	
	private static Integer readMyMessage(String msgCouponId) {
		try {
//			String url = URL_LOGIN + "&filter=username='" + strUserName + "'";
			String uri = Uri.parse(URL.URL_SEND_COUPON)
//					String uri = Uri.parse(URL_SEND_COUPON)
                .buildUpon()
                .appendQueryParameter(General.FILTER, Coupon.COUPON_ID + "=" + msgCouponId)
//                .appendQueryParameter(General.ORDER, Message.MESSAGE_CREATED_DATE + " desc")
                .build().toString();
			
			Log.i(MessageWebService.class.getName(), "readMyMessage(); GET MY Message Url = " + uri);
			HttpGet httpGet = new HttpGet(uri);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
 
//            httpGet.setEntity(se);
			httpGet = MyGetClient.setHeaders(httpGet);            
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(MessageWebService.class.getName(), "readMyMessage(); read my message response = " + strResponse);
			
			return MessageParser.parseMyMessages(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(MessageWebService.class, e, 0);
            return Result.FAIL;
		}
	}

	public static Integer decreaseUsesLimit(String msgCouponId, String msgCouponUsesLimit) {
		// TODO update so it works
		try {
			HttpPatch httpPatch = new HttpPatch(URL.URL_SEND_COUPON);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;

			JSONObject jsonObject = new JSONObject();
			JSONObject jsonRecord = new JSONObject();
			jsonRecord.accumulate(Coupon.COUPON_ID, msgCouponId);
			jsonRecord.accumulate(Coupon.COUPON_USES_LIMIT, Integer.valueOf(msgCouponUsesLimit) - 1);
			jsonObject.accumulate(General.RECORD, jsonRecord);

			String json = jsonRecord.toString();
			StringEntity se = new StringEntity(json);

			httpPatch.setEntity(se);
			httpPatch = MyGetClient.setHeaders(httpPatch);
			response = httpClient.execute(httpPatch);

			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(UserAsyncTask.class.getName(), "decreaseUsesLimit(); response:\n" + strResponse);

			return Result.SUCCESS;

		} catch (Exception e) {
			MyLogUtility.error(UserAsyncTask.class, e, 0);
			return Result.FAIL;
		}
	}
}
