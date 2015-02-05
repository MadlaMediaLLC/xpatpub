package com.han.xpatpub.model;

public class Message {
	
	public int msgID;
	public String msgText;
	public String msgCreatedDate;
	public String msgExpDate;
	public int msgStatus;
	public int msgSenderID;
	public int msgReceiverID;
	public int msgPubID;
	public int msgCouponID;
	public String msgCouponUsed;
	public Pub ownPub;
	public Coupon ownCoupon;
	
	public static final int MESSAGE_USED = 1;
	public static final int MESSAGE_NOT_USED = 0;
	
	public static final String MESSAGE_ID = "msgId";
	public static final String MESSAGE_TEXT = "msgText";
	public static final String MESSAGE_CREATED_DATE = "msgCreatedDate";
	public static final String MESSAGE_EXP_DATE = "msgExpDate";
	public static final String MESSAGE_STATUS = "msgStatus";
	public static final String MESSAGE_SENDER_ID = "msgSenderId";
	public static final String MESSAGE_RECEIVER_ID = "msgReceiverId";
	public static final String MESSAGE_PUB_ID = "msgPubId";
	public static final String MESSAGE_COUPON_ID = "msgCouponId";
	public static final String MESSAGE_COUPON_USED = "msgCouponUsed";
	public static final String MESSAGE_USED_TIME = "msgUsedTime";	
}

































