package com.han.xpatpub.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

import com.han.xpatpub.model.General;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Message;
import com.han.xpatpub.model.URL;
import com.han.xpatpub.utility.MyLogUtility;
import com.han.xpatpub.utility.TimeUtility;

public class MessageWebService extends MyGetClient {
	
	public static Integer addMessage(String msgText, String msgStatus, String msgSenderId, String msgReceiverId, String msgPubId, String msgCouponId) {
				
		try {
			HttpPost httpPost = new HttpPost(URL.URL_MESSAGE);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;
			
            JSONObject jsonObject = new JSONObject();
            
            jsonObject.accumulate(Message.MESSAGE_TEXT, msgText);
            jsonObject.accumulate(Message.MESSAGE_CREATED_DATE, TimeUtility.getCurrentTime(TimeUtility.DATE_FORMAT));
            jsonObject.accumulate(Message.MESSAGE_EXP_DATE, TimeUtility.getExpDate());
            jsonObject.accumulate(Message.MESSAGE_STATUS, "0");
            jsonObject.accumulate(Message.MESSAGE_SENDER_ID, msgSenderId);
            jsonObject.accumulate(Message.MESSAGE_RECEIVER_ID, msgReceiverId);
            jsonObject.accumulate(Message.MESSAGE_PUB_ID, msgPubId);
            jsonObject.accumulate(Message.MESSAGE_COUPON_ID, msgCouponId);
            jsonObject.accumulate(Message.MESSAGE_COUPON_USED, "0");	 

            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
 
            httpPost.setEntity(se);
            httpPost = MyGetClient.setHeaders(httpPost);            
			response = httpClient.execute(httpPost);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(MessageWebService.class.getName(), "addMessage(); add message response = " + strResponse);
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(MessageWebService.class, e, 0);
            return Result.FAIL;
		}
	}
	
	public static Integer readMyMessage() {
		try {
//			String url = URL_LOGIN + "&filter=username='" + strUserName + "'";
			String uri = Uri.parse(URL.URL_READ_MESSAGE)
//					String uri = Uri.parse(URL_SEND_COUPON)
                .buildUpon()
                .appendQueryParameter(General.FILTER, Message.MESSAGE_RECEIVER_ID + "=" + GlobalData.currentUser.userID)
                .appendQueryParameter(General.ORDER, Message.MESSAGE_CREATED_DATE + " desc")
                .appendQueryParameter(General.RELATED, "tblpubs_by_" 
                			+ Message.MESSAGE_PUB_ID 
                			+ ",tblcoupon_by_"
                			+ Message.MESSAGE_COUPON_ID)
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
	
	public static Integer markMessage(String msgId) {
		
		try {
			HttpPatch httpPatch = new HttpPatch(URL.URL_MESSAGE);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;

			JSONObject jsonObject = new JSONObject();
			JSONObject jsonRecord = new JSONObject();
			jsonRecord.accumulate(Message.MESSAGE_ID, msgId);
			jsonRecord.accumulate(Message.MESSAGE_STATUS, 1);
			jsonRecord.accumulate(Message.MESSAGE_COUPON_USED, 1);
			jsonRecord.accumulate(Message.MESSAGE_USED_TIME, TimeUtility.getCurrentTime(TimeUtility.DATE_FORMAT));
			jsonObject.accumulate(General.RECORD, jsonRecord);

            String json = jsonRecord.toString();
            StringEntity se = new StringEntity(json);
 
            httpPatch.setEntity(se);
            httpPatch = MyGetClient.setHeaders(httpPatch);
            
			response = httpClient.execute(httpPatch);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(MessageWebService.class.getName(), "markMessage(); mark message response = " + strResponse);
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(MessageWebService.class, e, 0);
			return Result.FAIL;
		}
	}
	
	public static Integer sendCouponMessage(String msgId) {
		
		try {
			HttpPatch httpPatch = new HttpPatch(URL.URL_MESSAGE);
			HttpClient httpClient = MyGetClient.getClient();
			HttpResponse response = null;

			JSONObject jsonObject = new JSONObject();
			JSONObject jsonRecord = new JSONObject();
			jsonRecord.accumulate(Message.MESSAGE_ID, msgId);
			jsonRecord.accumulate(Message.MESSAGE_STATUS, 1);
			jsonRecord.accumulate(Message.MESSAGE_COUPON_USED, 1);
			jsonRecord.accumulate(Message.MESSAGE_USED_TIME, TimeUtility.getCurrentTime(TimeUtility.DATE_FORMAT));
			jsonObject.accumulate(General.RECORD, jsonRecord);

            String json = jsonRecord.toString();
            StringEntity se = new StringEntity(json);
 
            httpPatch.setEntity(se);
            httpPatch = MyGetClient.setHeaders(httpPatch);            
			response = httpClient.execute(httpPatch);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			Log.i(MessageWebService.class.getName(), "markMessage(); mark message response = " + strResponse);
			
			return Result.SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(MessageWebService.class, e, 0);
			return Result.FAIL;
		}
	}
}
