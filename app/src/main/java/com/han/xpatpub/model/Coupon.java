package com.han.xpatpub.model;

public class Coupon {
	
	public int 		couponId;
	public int 		couponUserLimit;
	public int 		couponStatus;
	public int		couponUsesLimit;
	public int		couponUserId;
	public int 		couponPubId;
	public String 	couponName;
	public String 	couponType;
	public String 	couponCode;
	public String 	couponDescription;
	public String 	couponStartDate;
	public String 	couponExpireDate;	
	
	public static final String COUPON_ID = "couponId";
	public static final String COUPON_TYPE = "couponType";
	public static final String COUPON_CODE = "couponCode";
	public static final String COUPON_DESCRIPTION = "couponDescription";
	public static final String COUPON_START_DATE = "couponStartDate";
	public static final String COUPON_EXPIRE_DATE = "couponExpireDate";
	public static final String COUPON_USES_LIMIT = "couponUsesLimit";
	public static final String COUPON_USER_LIMIT = "couponUserLimit";
	public static final String COUPON_STATUS = "couponStatus";
	public static final String COUPON_USER_ID = "couponUserId";
	public static final String COUPON_PUB_ID = "couponPubId";
	public static final String COUPON_NAME = "couponName";
	
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public int getCouponUsesLimit() {
		return couponUsesLimit;
	}
	public void setCouponUsesLimit(int couponUsesLimit) {
		this.couponUsesLimit = couponUsesLimit;
	}	
}