package com.han.xpatpub.asynctasks;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.han.xpatpub.activity.ResultActivity;
import com.han.xpatpub.app.LocationUtility;
import com.han.xpatpub.model.Action;
import com.han.xpatpub.model.Advertise;
import com.han.xpatpub.model.Coupon;
import com.han.xpatpub.model.General;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.URL;
import com.han.xpatpub.network.MessageParser;
import com.han.xpatpub.network.MyGetClient;
import com.han.xpatpub.network.Result;
import com.han.xpatpub.utility.MyLogUtility;

public class FileAsyncTask extends AbstractedAsyncTask {
	
    public FileAsyncTask(Activity activity) {
		super(activity);
	}
    
    public FileAsyncTask(Context context) {
		super(context);
	}
	
	protected Integer doInBackground(String... params) {
		nResult = Result.FAIL;
		
		curAction = params[0];
		if (curAction.equals(Action.FILE_UPDATE_PUB_ICON)) {
			String filePath = params[1];
			String pubID = params[2];
			
			nResult = updatePubIcon(filePath, pubID);
			
		} else if (curAction.equals(Action.FILE_COUPON_LIST)) {
			nResult = readCouponList();
			
//		} else if (curAction.equals(Action.FILE_SEND_MESSAGE)) {
//			String msgText = params[1];
//			String msgRecieverID = params[2];
//			String msgCouponID = params[3];
//			
//			nResult = sendMessage(msgText, msgRecieverID, msgCouponID);
			
		} else if (curAction.equals(Action.FILE_ADVERTISE_LIST)) {
			getAdvertiseList();
			
		} else if (curAction.equals(Action.FILE_GET_CURRENT_CITY)) {
			getCurrentCity();
		}
		
		return nResult;
	}

	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
//		if (dlgLoading.isShowing())
//			dlgLoading.dismiss();
//		if (curAction.equals(Action.FILE_SEND_MESSAGE)) {
//			CouponActivity couponActivity = (CouponActivity) parent;
//			DialogUtility.showGeneralAlert(couponActivity, "Sent Coupon", "Your offer has been sent!");
			
//		} else 
		if (curAction.equals(Action.FILE_GET_CURRENT_CITY)) {
			if(activity instanceof ResultActivity) {
				((ResultActivity) activity).onReceive();
			}
		}
	}
	
	public Integer updatePubIcon(String filePath, String pubID) {
		try {
			String url = URL.URL_UPDATE_PUB_ICON + "&pubId=" + pubID;			
			
			// TODO This is a potential error code
			Log.i(FileAsyncTask.class.getName(), "update pub icon url = " + url);
			url = "http://xpatpub.com/api/json.php?action=updatePubIcon&pubId=" + pubID;
			Log.i(FileAsyncTask.class.getName(), "update pub icon url = " + url);
			
			HttpPost httpPost = new HttpPost(url);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = null;
			
//			httpPost.setHeader("Accept", "application/json");
//			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("X-DreamFactory-Session-Token", GlobalData.currentUser.session_id);
            
//			Log.e("default_url", Environment.getExternalStorageState());
			Log.i(FileAsyncTask.class.getName(), "file_path = " + filePath);
			
            File inputFile = new File(filePath);
            if (inputFile.exists()) {
				MultipartEntity reqEntity = new MultipartEntity();
//                reqEntity.addPart("file_name", new StringBody("a.jpg"));
                reqEntity.addPart("pubIcon", new FileBody(inputFile));
                httpPost.setEntity(reqEntity);
                
    			response = httpClient.execute(httpPost);
    			
    			String strResponse = EntityUtils.toString(response.getEntity());
    			MyLogUtility.info(MessageParser.class, curAction, strResponse);
            }

		} catch(Exception e) {
			MyLogUtility.error(MessageParser.class, e, 0);
            return Result.FAIL;
		}
		
		return Result.SUCCESS;
	}
	
	public Integer readCouponList() {
		try {
			
			String uri = Uri.parse(URL.URL_COUPON_LIST)
	                .buildUpon()
	                .appendQueryParameter(General.FILTER, Coupon.COUPON_USER_ID + "=" + GlobalData.currentUser.userID)
	                .build().toString();
			Log.i(FileAsyncTask.class.getName(), "readCouponList(); GET Coupon List Url = " + uri);
			
			HttpGet httpGet = new HttpGet(uri);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;

//	        httpGet.setEntity(se);
			httpGet = MyGetClient.setHeaders(httpGet);  
	        
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			MyLogUtility.info(MessageParser.class, curAction, strResponse);
			
			return parseCouponList(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(MessageParser.class, e, 0);
	        return Result.FAIL;
		}
	}
	
	public Integer parseCouponList(String jsonStr) {
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			
			JSONArray arrJsonCoupon = jsonObj.getJSONArray(General.RECORD);
			ArrayList<Coupon> arrCoupon = new ArrayList<Coupon>();
			  
			for (int i = 0; i < arrJsonCoupon.length(); i++) {
				
				Coupon coupon = new Coupon();
				
				JSONObject jsonCoupon = arrJsonCoupon.getJSONObject(i);
				
				coupon.couponId 			= jsonCoupon.getInt(Coupon.COUPON_ID);
				coupon.couponType 			= jsonCoupon.getString(Coupon.COUPON_TYPE);
				coupon.couponCode 			= jsonCoupon.getString(Coupon.COUPON_CODE);
				coupon.couponDescription 	= jsonCoupon.getString(Coupon.COUPON_DESCRIPTION);
				coupon.couponStartDate 		= jsonCoupon.getString(Coupon.COUPON_START_DATE);
				coupon.couponExpireDate 	= jsonCoupon.getString(Coupon.COUPON_EXPIRE_DATE);
				coupon.couponUsesLimit 		= jsonCoupon.getInt(Coupon.COUPON_USES_LIMIT);
				coupon.couponUserLimit 		= jsonCoupon.getInt(Coupon.COUPON_USER_LIMIT);
				coupon.couponStatus 		= jsonCoupon.getInt(Coupon.COUPON_STATUS);
				coupon.couponUserId 		= jsonCoupon.getInt(Coupon.COUPON_USER_ID);
				coupon.couponPubId 			= jsonCoupon.getInt(Coupon.COUPON_PUB_ID);
				coupon.couponName 			= jsonCoupon.getString(Coupon.COUPON_NAME);
				
				arrCoupon.add(coupon);
			}
			
			GlobalData.arrMyCoupon = arrCoupon;
			
			Log.i(FileAsyncTask.class.getName(), "My Coupon Count = " + Integer.toString(GlobalData.arrMyCoupon.size()));
			return Result.SUCCESS;
			
		} catch (Exception e) {
			
			MyLogUtility.error(MessageParser.class, e, 1);
			return Result.FAIL;
		}
	}
	
//	private Integer sendMessage(String msgText, String msgRecieverID, String msgCouponID) {
//		try {
//			Log.e("Send Message Url", URL.URL_SEND_MESSAGE);
//			
//			HttpPost httpPost = new HttpPost(URL.URL_SEND_MESSAGE);
//			HttpClient httpClient = MyGetClient.getClient();
//			HttpResponse response = null;
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.accumulate("msgText", msgText);
//            jsonObject.accumulate("msgCreatedDate", TimeUtility.getCurrentTime());
//            jsonObject.accumulate("msgExpDate", TimeUtility.getDateAfter(20));
//            jsonObject.accumulate("msgStatus", "0");
//            jsonObject.accumulate("msgSenderId", GlobalData.currentUser.userID);
//            jsonObject.accumulate("msgReceiverId", "1");
//            jsonObject.accumulate("msgPubId", GlobalData.ownPub.id);
//            jsonObject.accumulate("msgCouponId", msgCouponID);
//
//            String json = jsonObject.toString();
//            StringEntity se = new StringEntity(json);
// 
//            httpPost.setEntity(se);
//            httpPost = MyGetClient.setHeaders(httpPost);  
//            
//			response = httpClient.execute(httpPost);
//			
//			String strResponse = EntityUtils.toString(response.getEntity());
//			MyLogUtility.info(MessageParser.class, curAction, strResponse);
//			return Result.SUCCESS;
//			
//		} catch (Exception e) {
//			MyLogUtility.error(MessageParser.class, e, 0);
//            return Result.FAIL;
//		}
//	}
	
	public Integer getAdvertiseList() {
		try {			
			String url = URL.URL_ADVERTISE_LIST + "&Lat=" + Double.toString(LocationUtility.getCurLat(activity)) 
					+ "&Long=" + Double.toString(LocationUtility.getCurLng(activity)) 
					+ "&Radius=3.3" + "&session_id=" + GlobalData.currentUser.session_id;
			
//			String url = URL_ADVERTISE_LIST + "&Lat=44.853743&Long=-93.432202&Radius=3.3" + "&session_id=" + GlobalData.currentUser.session_id; 
			
			Log.e("GET Advertise List Url = ", url);
			
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;

//	        httpGet.setEntity(se);
			httpGet = MyGetClient.setHeaders(httpGet);  
	        
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			MyLogUtility.info(MessageParser.class, curAction, strResponse);
			
			return parseAdvertiseList(strResponse);
			
		} catch (Exception e) {
			MyLogUtility.error(MessageParser.class, e, 0);
	        return Result.FAIL;
		}
	}
	
	public Integer parseAdvertiseList(String jsonStr) {
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			
			JSONArray arrJsonAdvertise = jsonObj.getJSONArray(General.RECORD);
			ArrayList<Advertise> arrAdvertise = new ArrayList<Advertise>();
			  
			for (int i = 0; i < arrJsonAdvertise.length(); i++) {
				
				Advertise advertise = new Advertise();
				
				JSONObject jsonAdvertise = arrJsonAdvertise.getJSONObject(i);
				
				advertise.id 		= jsonAdvertise.getInt(Advertise.AD_ID);
				advertise.adIcon 	= jsonAdvertise.getString(Advertise.AD_ICON);
				advertise.time 		= jsonAdvertise.getString(Advertise.AD_TIME);
				advertise.lat 		= jsonAdvertise.getDouble(Advertise.AD_LATITUDE);
				advertise.lng 		= jsonAdvertise.getDouble(Advertise.AD_LONGITUDE);
				advertise.title 	= jsonAdvertise.getString(Advertise.AD_TITLE);
				advertise.type 		= jsonAdvertise.getString(Advertise.AD_TYPE);
				advertise.link 		= jsonAdvertise.getString(Advertise.AD_LINK);
				
				arrAdvertise.add(advertise);
			}
			
			// DON'T REMOVE THIS
			// Adds empty field to array to show "Request to Advertise" row bellow other adds
			arrAdvertise.add(null);
			
			GlobalData.arrAdvertise = arrAdvertise;
			
			Log.i(FileAsyncTask.class.getName(), "My Advertise Count = " + Integer.toString(GlobalData.arrAdvertise.size()));
			return Result.SUCCESS;
			
		} catch (Exception e) {			
			MyLogUtility.error(MessageParser.class, e, 1);
			return Result.FAIL;
		}
	}
	
	public Integer getCurrentCity() {
		try {			
			String url = URL.URL_GET_CURRENT_CITY 
					+ "&Lat=" + LocationUtility.getCurLat(activity) 
					+ "&Long=" + LocationUtility.getCurLng(activity); 
			
			Log.i(FileAsyncTask.class.getName(), "GET current city Url = " + url);
			
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
	        
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			
			strResponse = FileAsyncTask.chomp(strResponse);
			MyLogUtility.info(MessageParser.class, curAction, strResponse);
			
			GlobalData.curCityName = strResponse;
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(MessageParser.class, e, 0);
	        return Result.FAIL;
		}
	}
	
	/**
	 * <code>\u000a</code> linefeed LF ('\n').
	 * 
	 * @see <a
	 *      href="http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#101089">JLF:
	 *      Escape Sequences for Character and String Literals</a>
	 * @since 2.2
	 */
	public static final char LF = '\n';

	/**
	 * <code>\u000d</code> carriage return CR ('\r').
	 * 
	 * @see <a
	 *      href="http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#101089">JLF:
	 *      Escape Sequences for Character and String Literals</a>
	 * @since 2.2
	 */
	public static final char CR = '\r';

	public static String chomp(String str) {
		if (isEmpty(str)) {
			return str;
		}

		if (str.length() == 1) {
			char ch = str.charAt(0);
			if (ch == CR || ch == LF) {
				return "";
			}
			return str;
		}

		int lastIdx = str.length() - 1;
		char last = str.charAt(lastIdx);

		if (last == LF) {
			if (str.charAt(lastIdx - 1) == CR) {
				lastIdx--;
			}
		} else if (last != CR) {
			lastIdx++;
		}
		return str.substring(0, lastIdx);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
}
