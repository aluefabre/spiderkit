package org.fabrelab.pagekit.baidu;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.cookie.Cookie;
import org.fabrelab.pagekit.URLHelper;

public class BaiduSession {
	private static final String UTDATA = "28,29,26,5,24,27,26,32,5,5,5,5,5,5,5,5,5,32,26,31,25,25,25,26,32,24,16,27,25,5,24,25,17,25,13342309185410";
	private static final String tokenGenerateUrl = "https://passport.baidu.com/v2/api/?getapi&class=login&tpl=ik&tangram=false";
	private static final String questionUrl = "http://zhidao.baidu.com/question/408645737.html?seed=0";
	private static final String loginPageUrl = "http://zhidao.baidu.com/html/userlogin.html?t="+ System.currentTimeMillis();
	private static final String loginPostUrl = "https://passport.baidu.com/v2/api/?login";
	private static final String encoding = "gbk";
	
	boolean isLogin = false;
	URLHelper helper = new URLHelper(1, 1000);
	Pattern tokenPattern = Pattern.compile("bdPass.api.params.login_token='([^']+)';");
	Pattern qidPattern = Pattern.compile("\\\"qid\\\"\\s:\\s\\\"(\\d+)\\\"");

	public boolean login(String userName, String password){
		helper.setReferer(questionUrl);
		helper.getUrl(questionUrl, encoding);
		helper.printLastResult();
		
		helper.setReferer(loginPageUrl);
		String getpassport = helper.getUrl(tokenGenerateUrl, encoding);
		helper.printLastResult();
		
		Matcher matcher = tokenPattern.matcher(getpassport);
		String token = "";
		while(matcher.find()){
			token =  matcher.group(1);
		}
		
		Map<String, String> payload = buildLoginPostPayload(userName, password, token);
		
		helper.setReferer(loginPageUrl);
		helper.postUrl(loginPostUrl, payload, encoding);
		helper.printLastResult();
				
		for(Cookie cookie : helper.getCookieStore().getCookies()){
			if("BDUSS".equalsIgnoreCase(cookie.getName())){
				isLogin = true;
				return true;
			}
		}
		return false;
	}

	private Map<String, String> buildLoginPostPayload(String userName, String password, String token) {
		Map<String, String> payload = new LinkedHashMap<String, String>();

//		payload.put("charset", "GB2312");
//		payload.put("codestring", "");
		payload.put("token", token);
//		payload.put("isPhone", "false");
//		payload.put("index", "0");
//		payload.put("u", questionUrl);
//		payload.put("safeflg", "0");
//		payload.put("staticpage", "http://zhidao.baidu.com/html/jump.html");
//		payload.put("loginType", "1");
		payload.put("tpl", "ik");
//		payload.put("callback", "parent.bdPass.api.login._postCallback");
		payload.put("username", userName);
		payload.put("password", password);
//		payload.put("verifycode", "");
//		payload.put("mem_pass", "on");
		
		return payload;
	}
	
	public String publishQuestion(String content, int wealth){
		if(isLogin){
			
			Map<String, String> payload = new HashMap<String, String>();
			payload.put("query", content);
			payload.put("title", content);
			payload.put("cm", "100001");
			payload.put("cid", "171");
			payload.put("qid", "409744086");
			payload.put("wealth", wealth+"");
			payload.put("utdata", UTDATA);
			
			helper.postUrl("http://zhidao.baidu.com/submit/ajax?jasmine=1", payload , "utf-8");
			helper.printLastResult();
			
			String lastResult = helper.getLastAccessResult();
			Matcher matcher = qidPattern.matcher(lastResult);
			String qid = "";
			while(matcher.find()){
				qid =  matcher.group(1);
			}
			
			payload = new HashMap<String, String>();
			payload.put("title", content);
			payload.put("cm", "100001");
			payload.put("cid", "171");
			payload.put("qid", qid);
			helper.postUrl("http://zhidao.baidu.com/submit/?cm=100001", payload , encoding);

			helper.printLastResult();
			
		}
		return "";
	}
	
	public String publishAnswer(String qid, String content){
		if(isLogin){
			Map<String, String> payload = new HashMap<String, String>();
			payload.put("utdata", UTDATA);
			payload.put("cm", "100009");
			payload.put("cid", "183");
			payload.put("co", content);
			payload.put("qid", qid);
			helper.postUrl("http://zhidao.baidu.com/submit/ajax/",  payload , "utf-8");

			helper.printLastResult();
		}
		return "";
	}
	
	public static void main(String argv[]){
		
		BaiduSession session = new BaiduSession();
		session.login("aluefabre", "23061747");
		session.publishQuestion("那个为什么男孩子会长青春痘？",0);
		
		//session.publishAnswer("409693166", "贴个照片让大家鉴定下");
	}
}
