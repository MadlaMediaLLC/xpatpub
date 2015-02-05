package com.han.xpatpub.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.han.xpatpub.model.Coupon;
import com.han.xpatpub.model.General;
import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.Message;
import com.han.xpatpub.model.Pub;
import com.han.xpatpub.utility.MyLogUtility;

public class MessageParser extends Result {
	
	public static Integer parseMyMessages(String strResponse) {
		try {
			JSONObject jsonObj = new JSONObject(strResponse);
			
			JSONArray arrJsonMessage = jsonObj.getJSONArray(General.RECORD);
			ArrayList<Message> arrMessage = new ArrayList<Message>();
			
			for (int i = 0; i < arrJsonMessage.length(); i++) {
				JSONObject jsonMessage = arrJsonMessage.getJSONObject(i);
				
				Message message = new Message();
				message.msgID 			= jsonMessage.getInt(Message.MESSAGE_ID);
				message.msgText 		= jsonMessage.getString(Message.MESSAGE_TEXT);
				message.msgCreatedDate 	= jsonMessage.getString(Message.MESSAGE_CREATED_DATE);
                if(!jsonMessage.isNull(Message.MESSAGE_EXP_DATE)){
                    message.msgExpDate 	    = jsonMessage.getString(Message.MESSAGE_EXP_DATE);
                }
				message.msgStatus 		= jsonMessage.getInt(Message.MESSAGE_STATUS);
				message.msgSenderID		= jsonMessage.getInt(Message.MESSAGE_SENDER_ID);
				message.msgReceiverID 	= jsonMessage.getInt(Message.MESSAGE_RECEIVER_ID);
				message.msgPubID		= jsonMessage.getInt(Message.MESSAGE_PUB_ID);
				message.msgCouponID		= jsonMessage.getInt(Message.MESSAGE_COUPON_ID);
				message.msgCouponUsed 	= jsonMessage.getString(Message.MESSAGE_COUPON_USED);
				
				JSONObject jsonPub = jsonMessage.getJSONObject("tblpubs_by_msgPubId");
				Pub pub = new Pub();

				pub.id 				= jsonPub.getInt(Pub.PUB_ID);
				pub.name 			= jsonPub.getString(Pub.PUB_NAME);
				pub.lat 			= jsonPub.getDouble(Pub.PUB_LATITUDE);
				pub.lng 			= jsonPub.getDouble(Pub.PUB_LONGITUDE);
				pub.country 		= jsonPub.getInt(Pub.PUB_COUNTRY);
				pub.category 		= jsonPub.getInt(Pub.PUB_CATEGORY);
				pub.type 			= jsonPub.getInt(Pub.PUB_TYPE);
				pub.iconUrl 		= jsonPub.getString(Pub.PUB_ICON);
				pub.isHaveLiveMusic = (jsonPub.getInt(Pub.PUB_HAVE_LIVE_MUSIC) == 1);
				pub.isHaveTVSports 	= (jsonPub.getInt(Pub.PUB_HAVE_TV_SPORTS) == 1);
				pub.isHavePubGames 	= (jsonPub.getInt(Pub.PUB_HAVE_POOL_DARTS) == 1);
				pub.recommendYes 	= jsonPub.getInt(Pub.PUB_RECOMMEND_YES);
				pub.recommendNo 	= jsonPub.getInt(Pub.PUB_RECOMMEND_NO);
				pub.totalView 		= jsonPub.getInt(Pub.PUB_TOTAL_VIEWS);
				pub.founder 		= jsonPub.getInt(Pub.PUB_FOUNDER);
				pub.owner 			= jsonPub.getInt(Pub.PUB_OWNER);
				
				message.ownPub = pub;
				
				JSONObject jsonCoupon = jsonMessage.getJSONObject("tblcoupon_by_msgCouponId");
				Coupon coupon = new Coupon();

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
				
				message.ownCoupon = coupon;
				
				arrMessage.add(message);
			}
			
			GlobalData.arrMyMessage = arrMessage;
			Log.i(MessageWebService.class.getName(), 
					"parseMyMessages(); my message count = " 
							+ Integer.toString(arrMessage.size()));
			
			return SUCCESS;
			
		} catch (Exception e) {
			MyLogUtility.error(MessageParser.class, e, 1);
			return FAIL;
		}
	}
}
