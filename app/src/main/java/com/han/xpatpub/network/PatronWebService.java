package com.han.xpatpub.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.han.xpatpub.model.GlobalData;
import com.han.xpatpub.model.URL;
import com.han.xpatpub.utility.MyLogUtility;

public class PatronWebService extends MyGetClient {	

	public static Integer searchUser(String strLat, String strLng, String strRadius) {
		try {
			String url = URL.URL_CUSTOM + "&action=getNearUsersByPubs" +"&Lat=" + strLat + "&Long=" + strLng/* + "&session_id=" + GlobalData.currentUser.session_id*/;
			
			if (!strRadius.equals("")) {
				url += ("&Radius=" + strRadius);
			}
			
			Log.i(PatronWebService.class.getName(), "Url = " + url);
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = getClient();
			HttpResponse response = null;
	
	//        httpGet.setEntity(se);
	        httpGet.setHeader("Accept", "application/json");
	        httpGet.setHeader("Content-type", "application/json");
	        httpGet.setHeader("X-DreamFactory-Session-Token", GlobalData.currentUser.session_id);
	        
			response = httpClient.execute(httpGet);
			
			String strResponse = EntityUtils.toString(response.getEntity());
			
			Log.i(PatronWebService.class.getName(), "search user response\n" + strResponse);
			return PatronParser.parseSearchUser(strResponse);	
			
		} catch (Exception e) {
			MyLogUtility.error(PatronWebService.class, e, 0);
	        return Result.FAIL;
		}
	}
}