package com.han.xpatpub.network;

import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.han.xpatpub.model.GlobalData;

public class MyGetClient {
	
	public static DefaultHttpClient getClient() {
		DefaultHttpClient ret = null;
		// sets up parameters
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");		
//		HttpProtocolParams.setHttpElementCharset(params, "UTF-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);

		// registers schemes for both http and https
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
		
		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
		ret = new DefaultHttpClient(manager, params);
		return ret;
	}
	
	public static HttpGet setHeaders(HttpGet httpGet) {
        return (HttpGet) setHeadersAbstract(httpGet);
	}
	
	public static HttpPatch setHeaders(HttpPatch httpPatch) {
        return (HttpPatch) setHeadersAbstract(httpPatch);
	}
	
	public static HttpPost setHeaders(HttpPost httpPost) {
        return (HttpPost) setHeadersAbstract(httpPost);
	}

	public static HttpDelete setHeaders(HttpDelete httpDelete) {	
        return (HttpDelete) setHeadersAbstract(httpDelete);
	}
	
	private static AbstractHttpMessage setHeadersAbstract(AbstractHttpMessage httpDelete) {
		httpDelete.setHeader("Accept", "application/json");
		httpDelete.setHeader("Content-type", "application/json");
		httpDelete.setHeader("X-DreamFactory-Session-Token", GlobalData.currentUser.session_id);
		
		return httpDelete;
	}
}
