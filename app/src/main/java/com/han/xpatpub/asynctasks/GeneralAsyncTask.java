package com.han.xpatpub.asynctasks;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.han.xpatpub.activity.CouponActivity;
import com.han.xpatpub.activity.LoginActivity;
import com.han.xpatpub.activity.MessageActivity;
import com.han.xpatpub.activity.ResultActivity;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.General;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Pub;
import com.han.xpatpub.model.URL;
import com.han.xpatpub.model.User;
import com.han.xpatpub.network.MessageWebService;
import com.han.xpatpub.network.MyGetClient;
import com.han.xpatpub.network.PatronWebService;
import com.han.xpatpub.network.Result;
import com.han.xpatpub.utility.MyLogUtility;

public class GeneralAsyncTask extends AbstractedAsyncTask {
       private String error;

	public GeneralAsyncTask(Activity activity) {
		super(activity);
	}
	
	public GeneralAsyncTask(Context context) {
		super(context);
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		nResult = Result.FAIL;
		
		curAction = params[0];
		
		if (curAction.equals(Action.ACTION_SESSION)) {
			String email = params[1];
			String password = params[2];
			nResult = session(email, password);
			
		}  else if (curAction.equals(Action.ACTION_PUB_OWNER_DETAIL)) {
			nResult = pubOwnerDetail();
			
		} else if (curAction.equals(Action.ACTION_GET_COUNTRY_CODE)) {
			nResult = setCountryCode();
			
		} else if (curAction.equals(Action.ACTION_GET_COUNTRY_CODE_FROM_GEO_DATA)) {
			String strLat = params[1];
			String strLng = params[2];
			nResult = setCountryCodeFromGeoData(strLat, strLng);
			
		} else if (curAction.equals(Action.FILE_GET_CURRENT_CITY)) {
//			nResult = setCurrentCity();
			
//		} else if (curAction.equals(ACTION_UPDATE_FEATURE)) {
//			Pub  = Integer.parseInt(params[1]);
//			
//			nResult = PubWebService.deletePubFeaure(nPubID);
//			nResult = Parser.SUCCESS;
//
//			String strPubFeature = params[2];
//			
//			if (nResult == Parser.SUCCESS) {
//				String[] arrStr = strPubFeature.split(",");
//				
//				for (int i = 0; i < arrStr.length; i++) {
//					nResult = PubWebService.addPubFeature(nPubID, Integer.parseInt(arrStr[i]));
//					if (nResult == Parser.FAIL) {
//						return nResult;
//					}
//				}
//			}
			
		} else if (curAction.equals(Action.ACTION_USER_LIST)) {
			String strLat = params[1];
			String strLng = params[2];
			String strRadius = params[3];
			
			nResult = PatronWebService.searchUser(strLat, strLng, strRadius);
			
		} else if (curAction.equals(Action.ACTION_CREATE_MESSAGE)) {
			String msgText = params[1];
			String msgStatus = "0";
			String msgSenderId = params[2];
			String msgReceiverId = params[3];
			String msgPubId = params[4];
			String msgCouponId = params[5];			

			nResult = MessageWebService.addMessage(msgText, msgStatus, msgSenderId, msgReceiverId, msgPubId, msgCouponId);
			if (nResult == Result.SUCCESS) {				
				nResult = MessageWebService.readMyMessage();
			}
			
		} else if (curAction.equals(Action.ACTION_MARK_MESSAGE)) {
			String msgId = params[1];
            String status = params[2];
			nResult = MessageWebService.markMessage(msgId,Integer.valueOf(status));
			if (nResult == Result.SUCCESS) {
				nResult = MessageWebService.readMyMessage();
			}
			
		} else if (curAction.equals(Action.ACTION_USER_MESSAGE)) {
			nResult = MessageWebService.readMyMessage();
			
		} else if (curAction.equals(Action.OWNER_SEND_COUPON)) {
			String msgText = params[1];
			String msgStatus = "0";
			String msgSenderId = params[2];
			String msgReceiverId = params[3];
			String msgPubId = params[4];
			String msgCouponId = params[5];
			String msgCouponUsesLimit = params[6];

			nResult = MessageWebService.addMessage(msgText, msgStatus, msgSenderId, msgReceiverId, msgPubId, msgCouponId);
			
			if (nResult == Result.SUCCESS) {					
				nResult = UserAsyncTask.decreaseUsesLimit(msgSenderId);
				
				if (nResult == Result.SUCCESS) {				
					nResult = MessageWebService.readMyMessage();
				}
			}			
		} else if (curAction.equals(Action.OWNER_RETURN_COUPON)) {
            String msgSenderId = params[1];

            nResult = UserAsyncTask.increaseUsesLimit(msgSenderId);
            if (nResult == Result.SUCCESS) {
                nResult = MessageWebService.readMyMessage();
            }

        }

        return nResult;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
//		parent.startActivity(new Intent(parent, CompleteActivity.class));

        if (curAction.equals(Action.ACTION_SEND_NONCE)) {
            if(nResult == Result.FAIL){
                Toast.makeText(activity, "Payment failed, reason: " + error, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(activity,"Payment successful", Toast.LENGTH_LONG).show();
            }
        }
		if (curAction == Action.ACTION_SESSION) {
			LoginActivity.successSession(parent);
			
		} else if (curAction == Action.ACTION_PUB_OWNER_DETAIL) {
			if (nResult == Result.SUCCESS) {
				
			}
			
		} else if (curAction.equals(Action.ACTION_USER_MESSAGE)) {
			if (nResult == Result.SUCCESS) {
				if (activity instanceof ResultActivity) {
					((ResultActivity) activity).setUnreadMessageCount();
				}
			}
			
		} else if (curAction.equals(Action.ACTION_MARK_MESSAGE)) {
			if (nResult == Result.SUCCESS) {
				if (activity instanceof ResultActivity) {
					((ResultActivity) activity).setUnreadMessageCount();
				}
				
				MessageActivity messageActivity = (MessageActivity) parent;
				messageActivity.successMarkMessage();
			}
			
		} else if (curAction.equals(Action.ACTION_CREATE_MESSAGE)) {
			if (nResult == Result.SUCCESS) {
				if (activity instanceof ResultActivity) {
					((ResultActivity) activity).setUnreadMessageCount();
				}
			}			
		} else if (curAction.equals(Action.OWNER_SEND_COUPON)) {
			if (nResult == Result.SUCCESS) {
				if (activity instanceof CouponActivity) {
					((CouponActivity) activity).succesfulCouponSent();
				}
			}
		}
	}
	
	private Integer session(String email, String password) {
		
		try {
			HttpPost httpPost = new HttpPost(URL.URL_SESSION);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
			 
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("email", email);
            jsonObject.accumulate("password", password);
 
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
 
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            // TODO maybe third line is missing...?
			
			response = httpClient.execute(httpPost);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			MyLogUtility.info(GeneralAsyncTask.class, curAction, strResponse);
			return parseSession(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(GeneralAsyncTask.class, e, 0);
            return Result.FAIL;
		}		
	}
	
	private Integer parseSession(String jsonStr) {
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
//			GlobalData.currentUser.firstName = jsonObj.getString("first_name");
//			GlobalData.currentUser.lastName = jsonObj.getString("last_name");
			GlobalData.currentUser.session_id = jsonObj.getString("session_id");
//			GlobalData.currentUser.email = jsonObj.getString("email");
			
			Log.i(GeneralAsyncTask.class.getName(), "session_id = " + GlobalData.currentUser.session_id);
//			Log.e("FirstName and Last Name", GlobalData.currentUser.firstName + " " + GlobalData.currentUser.lastName);
		} catch (Exception e) {
			
			MyLogUtility.error(GeneralAsyncTask.class, e, 1);
			return Result.FAIL;
		}
		
		return Result.SUCCESS;
	}
	
	private Integer pubOwnerDetail() {
		try {
			String url = URL.URL_PUB_DETAIL + "&filter=pubOwner=" + GlobalData.currentUser.userID;
			
			Log.i(GeneralAsyncTask.class.getName(), "pub owner url = " + url);
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
 
//            httpGet.setEntity(se);
			httpGet = MyGetClient.setHeaders(httpGet);
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			MyLogUtility.info(GeneralAsyncTask.class, curAction, strResponse);
			
			return parsePubOwnerDetail(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(GeneralAsyncTask.class, e, 0);
            return Result.FAIL;
		}
	}
	
	private Integer parsePubOwnerDetail(String jsonStr) {
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONArray arrJsonPub = jsonObj.getJSONArray(General.RECORD);
			Pub pub = new Pub();
			
			JSONObject jsonPub = arrJsonPub.getJSONObject(0);

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
			
			GlobalData.ownPub = pub;
			
			Log.i(GeneralAsyncTask.class.getName(), "Own Pub name = " +  pub.name + ", id = " + Integer.toString(pub.id));
			return Result.SUCCESS;
			
		} catch (Exception e) {			
			MyLogUtility.error(GeneralAsyncTask.class, e, 1);
			return Result.FAIL;
		}
	}
		
	private Integer setCountryCode() {
		try {
			
			String url = URL.URL_GET_COUNTRY_CODE + "'" + GlobalData.isoCountryCode + "'";
			Log.e("GET COUNTRY CODE Url = ", url);
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
 
//            httpGet.setEntity(se);
			httpGet = MyGetClient.setHeaders(httpGet);
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			MyLogUtility.info(GeneralAsyncTask.class, curAction, strResponse);
			
			return parseCountryCode(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(GeneralAsyncTask.class, e, 0);
            return Result.FAIL;
		}
	}
	
	private Integer parseCountryCode(String jsonStr) {
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			
			JSONArray arrJsonCountry = jsonObj.getJSONArray(General.RECORD);
			JSONObject jsonCountry = arrJsonCountry.getJSONObject(0);
			
			GlobalData.countryNumber = jsonCountry.getString("countryId");
			
			Log.i(GeneralAsyncTask.class.getName(), "countryNumber = " + GlobalData.countryNumber);
			return Result.SUCCESS;
			
		} catch (Exception e) {			
			MyLogUtility.error(GeneralAsyncTask.class, e, 0);
			return Result.FAIL;
		}
	}
	
	private Integer setCountryCodeFromGeoData(String strLat, String strLng) {
		try {
			String url = URL.URL_CUSTOM + "&action=getGeoData" +"&Lat=" + strLat + "&Long=" + strLng + "&session_id=" + GlobalData.currentUser.session_id;
			
			Log.i(GeneralAsyncTask.class.getName(), "Country Code Url = " + url);
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
 
//            httpGet.setEntity(se);
			httpGet = MyGetClient.setHeaders(httpGet);            
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			MyLogUtility.info(GeneralAsyncTask.class, curAction, strResponse);
			GlobalData.countryNumber = strResponse;
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(GeneralAsyncTask.class, e, 0);
            return Result.FAIL;
		}
	}
}