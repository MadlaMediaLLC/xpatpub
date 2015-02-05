package com.han.xpatpub.model;

public class URL {
	
    public static final String URL_SESSION = General.IP_REST_USER + "/session?app_name=" + General.APP_NAME;
    public static final String URL_CUSTOM = General.IP_REST_GET_GENERIC_SERVICES + "/?app_name=" + General.APP_NAME;
//    public static final String URL_ADD_PUB = General.IP_REST_XPATPUB + "/tblpubs/?app_name=" + General.APP_NAME;
    public static final String URL_ADD_RATE = General.IP_REST_XPATPUB + "/tblrating/?app_name=" + General.APP_NAME;
    public static final String URL_GET_COUNTRY_CODE = General.IP_REST_XPATPUB + "/tblcountries?app_name=" + General.APP_NAME + "&filter=countryCode%3D";
    public static final String URL_PUB_DETAIL = General.IP_REST_XPATPUB + "/tblpubs?app_name=" + General.APP_NAME + "&include_count=true";
    public static final String URL_PUB = General.IP_REST_XPATPUB + "/tblpubs/?app_name=" + General.APP_NAME;
    public static final String URL_PUB_FEATURE = General.IP_REST_XPATPUB + "/tblpubfeatures/?app_name=" + General.APP_NAME;
    public static final String URL_PUB_RATE = General.IP_REST_XPATPUB + "/tblPubFeatureCounter/?app_name=" + General.APP_NAME;
//    public static final String URL_PUB_INFO_AND_RATE = General.IP_REST_XPATPUB + "/tblPubFeatureCounter";    
    public static final String URL_MESSAGE = General.IP_REST_XPATPUB + "/tblmessage?app_name=" + General.APP_NAME;
	public static final String URL_SEND_COUPON = General.IP_REST_XPATPUB + "/tblcoupon?app_name=" + General.APP_NAME;
    public static final String URL_SEND_COUPON_BY_ID = General.IP_REST_GET_GENERIC_SERVICES+"/?app_name="+General.APP_NAME+"&action=setUserCoupons";
	public static final String URL_READ_MESSAGE = General.IP_REST_XPATPUB + "/tblmessage?app_name=" + General.APP_NAME + "&include_count=true&msgStatus=0";
	public static final String URL_UPDATE_PUB_ICON = General.IP_REST_GET_GENERIC_SERVICES + "/?app_name=" + General.APP_NAME + "&action=updatePubIcon";
    public static final String URL_COUPON_LIST = General.IP_REST_XPATPUB + "/tblcoupon?app_name=" + General.APP_NAME;
    public static final String URL_SEND_MESSAGE = General.IP_REST_XPATPUB + "/tblmessage?app_name=" + General.APP_NAME;
    public static final String URL_ADVERTISE_LIST = General.IP + "/rest/getGenericServices/?app_name=" + General.APP_NAME + "&action=getNearByAds";
    public static final String URL_GET_CURRENT_CITY = "http://xpatpub.com/api/json.php?action=getCity";
	public static final String URL_LOGIN = General.IP_REST_XPATPUB + "/tblusers?app_name=" + General.APP_NAME;
	public static final String URL_REGISTER = General.IP_REST_XPATPUB + "/tblusers?app_name=" + General.APP_NAME;
//	public static final String URL_USER_PUB = General.IP_REST_XPATPUB + "/tblpubs?app_name=" + General.APP_NAME + "&include_count=true";
	public static final String URL_UPDATE_USER_PRIVACY = General.IP_REST_XPATPUB + "/tblusers?app_name=" + General.APP_NAME;
	public static final String URL_UPDATE_USER_LOCATION = General.IP_REST_XPATPUB + "/tblusers?app_name=" + General.APP_NAME;   
}
