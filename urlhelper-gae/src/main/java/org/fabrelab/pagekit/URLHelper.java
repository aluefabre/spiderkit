package org.fabrelab.pagekit;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class URLHelper {
	private static final Logger log = Logger.getLogger(URLHelper.class.getName());
	Integer retry;
	Integer delay;

	public URLHelper(Integer retry, Integer delay) {
		this.retry = retry;
		this.delay = delay;
	}

	public String getUrl(String url, String encoding) {
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
		String result = null;
		try {
			URLFetchService httpclient = URLFetchServiceFactory.getURLFetchService();
			HTTPRequest httpget = new HTTPRequest(new URL(url), HTTPMethod.GET);			
			httpget.addHeader(new HTTPHeader("Host", httpget.getURL().getHost()));
			httpget.addHeader(new HTTPHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.10) Gecko/20100914 Firefox/3.6.10"));
			httpget.addHeader(new HTTPHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
			httpget.addHeader(new HTTPHeader("Accept-Language", "zh-cn,zh;q=0.5"));
			httpget.addHeader(new HTTPHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7"));
			httpget.addHeader(new HTTPHeader("Keep-Alive", "115"));
			httpget.addHeader(new HTTPHeader("Connection", "keep-alive"));
			httpget.addHeader(new HTTPHeader("Cache-Control", "max-age=0"));			
			HTTPResponse response = httpclient.fetch(httpget);
			if (HttpStatus.SC_OK == response.getResponseCode()) {
				byte[] entity = response.getContent();
				result = new String(entity, encoding);
				log.fine(url + " opened, found the following content " + result);
			}
		} catch (Exception e) {
			log.warning("Could not open " + url);
		}
		return result;
	}

	public String postUrl(String url, Map<String,String> payloadMap, String encoding) {
		String payload = "";
		try {
			payload = buildPayload(payloadMap, encoding);
		} catch (UnsupportedEncodingException e) {
			log.warning("build payload failed...");
			return null;
		}
		for (int i = 0; i < retry; i++) {
			String html = tryPostUrl(url, payload);
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
	
	private String buildPayload(Map<String, String> payloadMap, String encoding) throws UnsupportedEncodingException {
		String payload = "";
		for(String key : payloadMap.keySet()){
			String value = payloadMap.get(key);
			payload += key+"="+URLEncoder.encode(value, encoding)+"&";
		}
		if(payload.length()>0){
			payload = payload.substring(0, payload.length()-1);
		}
		return payload;
	}

	public String tryPostUrl(String url, String payload){
		try {
			URLFetchService httpclient = URLFetchServiceFactory.getURLFetchService();
			HTTPRequest httppost = new HTTPRequest(new URL(url), HTTPMethod.POST);			
			httppost.addHeader(new HTTPHeader("Host", httppost.getURL().getHost()));
			httppost.addHeader(new HTTPHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.10) Gecko/20100914 Firefox/3.6.10"));
			httppost.addHeader(new HTTPHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
			httppost.addHeader(new HTTPHeader("Accept-Language", "zh-cn,zh;q=0.5"));
			httppost.addHeader(new HTTPHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7"));
			httppost.addHeader(new HTTPHeader("Keep-Alive", "115"));
			httppost.addHeader(new HTTPHeader("Connection", "keep-alive"));
			httppost.addHeader(new HTTPHeader("Cache-Control", "max-age=0"));
			httppost.addHeader(new HTTPHeader("Content-Type", "application/x-www-form-urlencoded"));
			httppost.addHeader(new HTTPHeader("Content-Encoding", "utf-8"));

			httppost.setPayload(payload.getBytes());
			HTTPResponse response = httpclient.fetch(httppost);
			if (HttpStatus.SC_OK == response.getResponseCode()) {
				byte[] entity = response.getContent();
				String result = new String(entity, "utf-8");
				return result;
			}else{
				byte[] entity = response.getContent();
				String result = new String(entity, "utf-8");
				log.warning(url + " access failed, server response is " + response.getResponseCode());
				log.warning(" server response is " + result);
			}
		} catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
		return null;
	}
}
