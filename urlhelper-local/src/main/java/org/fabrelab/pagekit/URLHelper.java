package org.fabrelab.pagekit;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

public class URLHelper {
	private static final Logger log = Logger.getLogger(URLHelper.class.getName());
	Integer retry;
	Integer delay;

	String referer;
	String lastAccessUrl;
	String lastAccessResult = null;
		
	DefaultHttpClient httpclient = new DefaultHttpClient();
	{
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY );
	}
	
	
	public URLHelper(Integer retry, Integer delay) {
		this.retry = retry;
		this.delay = delay;
	}

	public String getUrl(String url, String encoding) {
		lastAccessUrl = url;
		for (int i = 0; i < retry; i++) {
			String html = tryGetUrl(url, encoding);
			if (html != null) {
				return html;
			} else if (i < retry - 1) {
				try {
					Thread.sleep(delay * 1000);
				} catch (InterruptedException e) {
					log.warning(e.getMessage());
				}
				log.warning("Retrying to open " + url + ". Number: " + (i + 1));
			} else {
				log.warning("Give up...");
			}
		}
		return null;
	}
	

	private String tryGetUrl(String url, String encoding) {
		try {
			HttpGet httpget = new HttpGet(url); 	
			setHeaders(httpget);
			
			HttpResponse response = httpclient.execute(httpget);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				// If the response does not enclose an entity, there is no need
		        // to bother about connection release
		        if (entity != null) {
		        	lastAccessResult = MyEntityUtils.toString(entity, encoding);
		        	log.fine(url + " opened with GET, found the following content " + lastAccessResult);
		        }else{
		        	log.fine(url + " opened with GET, entity is null! ");
		        }
			}else if (HttpStatus.SC_MOVED_TEMPORARILY == response.getStatusLine().getStatusCode()) {
				Header[] headers = response.getAllHeaders();
				for(Header header : headers){
					if("Location".equals(header.getName())){
						lastAccessResult = header.getValue();
						log.fine(url + " opened with POST, found redirection result " + lastAccessResult);
						break;
					}
				}
			}else if (HttpStatus.SC_NOT_FOUND == response.getStatusLine().getStatusCode()) {
				return "HTTP/1.1 404 Not Found";
			}else if (HttpStatus.SC_INTERNAL_SERVER_ERROR == response.getStatusLine().getStatusCode()) {
				return "HTTP/1.1 500 Server Error";
			}else{
				log.log(Level.WARNING, response.getStatusLine().toString());
			}
		} catch (Exception e) {
			log.log(Level.WARNING, "Could not open " + url, e);
		}
		return lastAccessResult;
	}

	public String postUrl(String url, Map<String, String> payload, String encoding) {
		lastAccessUrl = url;
		for (int i = 0; i < retry; i++) {
			String html = tryPostUrl(url, payload, encoding);
			if (html != null) {
				return html;
			} else if (i < retry - 1) {
				try {
					Thread.sleep(delay * 1000);
				} catch (InterruptedException e) {
					log.warning(e.getMessage());
				}
				log.warning("Retrying to post " + url + ". Number: " + (i + 1));
			} else {
				log.warning("Give up...");
			}
		}
		return null;
	}
	
	
	public String tryPostUrl(String url, Map<String, String> payload, String encoding){
		try {

			HttpPost httppost = new HttpPost(url); 
		    httppost.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
			setHeaders(httppost);
			
			// Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for(String key : payload.keySet()){
				 nameValuePairs.add(new BasicNameValuePair(key, payload.get(key)));
			}	
	        UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(nameValuePairs, encoding);
			httppost.setEntity(reqEntity);
			
			HttpResponse response = httpclient.execute(httppost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				// If the response does not enclose an entity, there is no need
		        // to bother about connection release
		        if (entity != null) {
		        	lastAccessResult = MyEntityUtils.toString(entity, encoding);
		        	log.fine(url + " opened with POST, found the following content " + lastAccessResult);
		        }else{
		        	log.fine(url + " opened with POST, entity is null! ");
		        }
			}else if (HttpStatus.SC_MOVED_TEMPORARILY == response.getStatusLine().getStatusCode()) {
				Header[] headers = response.getAllHeaders();
				for(Header header : headers){
					if("Location".equals(header.getName())){
						lastAccessResult = header.getValue();
						log.fine(url + " opened with POST, found redirection result " + lastAccessResult);
						break;
					}
				}
			}else{
				log.log(Level.WARNING, response.getStatusLine().toString());
			}
		} catch (Exception e) {
			log.log(Level.WARNING, "Could not open " + url, e);
		}
		return lastAccessResult;
	}

	private void setHeaders(HttpRequestBase httppost) {
		httppost.addHeader(new BasicHeader("Host", httppost.getURI().getHost()));
		httppost.addHeader(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko/20100101 Firefox/11.0"));
		httppost.addHeader(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		httppost.addHeader(new BasicHeader("Accept-Language", "zh-cn,zh;q=0.5"));
		httppost.addHeader(new BasicHeader("Connection", "keep-alive"));
		httppost.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
		
		if(referer!=null){
			httppost.addHeader(new BasicHeader("Referer", referer));
		}else if(lastAccessUrl!=null){
			httppost.addHeader(new BasicHeader("Referer", lastAccessUrl));
		}
	}

	public CookieStore getCookieStore() {
		return httpclient.getCookieStore();
	}
	
	public void printLastResult(){
		System.out.println();
		System.out.println("lastAccessUrl : " + lastAccessUrl);
		if(lastAccessResult!=null){
			if(lastAccessResult.length()>500){
				System.out.println("lastAccessResult : " + lastAccessResult.substring(0, 500)  + " 。。。。。。");
			}else{
				System.out.println("lastAccessResult : " + lastAccessResult);
			}
		}else{
			System.out.println("lastAccessResult : " + "NULL");
		}
		System.out.println("Cookies : ");
		CookieStore cookieStore = getCookieStore();
		for(Cookie cookie : cookieStore.getCookies()){
			System.out.println("    " + cookie.getName() + " : " + cookie.getValue());
		}
		System.out.println();
		System.out.println("----------------------------------------------------------");
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getLastAccessResult() {
		return lastAccessResult;
	}
	
	
}
