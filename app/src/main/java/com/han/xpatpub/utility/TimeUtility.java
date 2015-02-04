package com.han.xpatpub.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtility {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String PART_DATE_FORMAT = "yyyy-MM-dd";
    public static final String CAMERA_TIME_STAMP = "yyyyMMdd_HHmmss";
    
    public static final TimeZone timeZone = TimeZone.getTimeZone("GMT");
	
	public static String getCurrentTime(String format) {		
		DateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
		df.setTimeZone(timeZone);
		String date = df.format(getCurrentDate(format, timeZone));
		
//		Log.i(TimeUtility.class.getName(), "userLastLogin = " + date);
		return date;
	}
	
	public static Date getCurrentDate(String format, TimeZone timeZone) {
		return Calendar.getInstance(timeZone).getTime();
	}
	
	public static String getDate(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
		df.setTimeZone(timeZone);		
		return df.format(date);
	}
	
	public static String getExpDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(TimeUtility.getCurrentDate(TimeUtility.DATE_FORMAT, TimeUtility.timeZone));
		c.add(Calendar.DATE, 1);
		return TimeUtility.getDate(c.getTime(), TimeUtility.DATE_FORMAT);
	}
	
//	public static String getDateAfter(int days) {		
//		TimeZone timeZone = TimeZone.getTimeZone("GMT");
//		Calendar nowCalendar = Calendar.getInstance(timeZone); 		
////		Log.e("dayOfWeek", Integer.toString(dayOfWeek));
//		
//		long nowTime = nowCalendar.getTimeInMillis();
//		long afterTime = nowTime + 86400000 * days;
//		
//		DateFormat df = new SimpleDateFormat(PART_DATE_FORMAT, Locale.ENGLISH);
//		df.setTimeZone(timeZone);
//		Date afterDate = new Date(afterTime);
//		String strDate = df.format(afterDate);
//		
//		return strDate;	
//	}
}
