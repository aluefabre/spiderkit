package org.fabrelab.pagekit.baiduzhidao.task;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.fabrelab.pagekit.baiduzhidao.model.BaiduZhidaoQuestion;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.springframework.util.StringUtils;

public class BaiduZhidaoProcessTask extends ProcessTask {

	private Pattern listPattern = Pattern.compile("(/q\\?ct=\\d+&tn=ikaslist&rn=\\d+&word=[^&]+&lm=\\d+&pn=)(\\d+)");
	private Pattern detailPattern = Pattern.compile("/question/\\d+\\.html");
	String word;
	public BaiduZhidaoProcessTask(TaskPool pool, Page page, String word) {
		super(pool, page);
		this.word = word;
	}
	
	public List<StoreTask> process() {
		System.out.println(Thread.currentThread().getId() + ": Processing " + page.getUrl());
		String content = fetch("gbk");
		List<StoreTask> result = new Vector<StoreTask>();
		if(page.getUrl().contains("question")){
			StoreTask stask = buildZhidaoQuestion(content, page.getUrl());
			result.add(stask);
		}
		
		Matcher detailMatcher = detailPattern.matcher(content);
		while(detailMatcher.find()){
			String detailURL = "http://zhidao.baidu.com"+StringEscapeUtils.unescapeHtml(detailMatcher.group(0));
			Page detailPage = new Page(detailURL, Page.STATUS_WAITING, 1);
			pool.addTask(detailPage);
		}
		
		Matcher listMatcher = listPattern.matcher(content);
		while(listMatcher.find()){
			try {
				String encoded = URLEncoder.encode(word, "GBK");
				String listURL = "http://zhidao.baidu.com"+StringEscapeUtils.unescapeHtml(listMatcher.group(0));
				if(listURL.contains(encoded)){
					Page listPage = new Page(listURL, Page.STATUS_WAITING, 1);
					pool.addTask(listPage);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}


	Pattern titlePattern = Pattern.compile("<span class=\"question-title\">(.+)</span>");
	Pattern contentPattern = Pattern.compile("<pre id=\"question-content\">(.+)</pre>");
	Pattern timePattern = Pattern.compile("<span class=\"gray\">(\\d+\\-\\d+\\-\\d+\\s+\\d+:\\d+)</span>");
	Pattern answerablePattern = Pattern.compile("<div class=\"clf\" id=\"answer-editor\"></div>");

	private StoreTask buildZhidaoQuestion(String content, String pageUrl) {
		
		Matcher matcher = titlePattern.matcher(content);
		String title = "";
		while(matcher.find()){
			title =  StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		String qcontent = "";
		String qcontentMark = "<pre id=\"question-content\">";
		int qcontentStart = content.indexOf(qcontentMark);
		if(qcontentStart>0){
			int qcontentEnd = content.indexOf("</pre>", qcontentStart);
			qcontent = content.substring(qcontentStart+qcontentMark.length(), qcontentEnd);
		}
		
		
		String qcontentMark2 = "<pre id=\"question-suply\">";
		int qcontentStart2 = content.indexOf(qcontentMark2);
		if(qcontentStart2>0){
			int qcontentEnd2 = content.indexOf("</pre>", qcontentStart2);
			qcontent = qcontent + " " + content.substring(qcontentStart2+qcontentMark2.length(), qcontentEnd2);
		}
		
		matcher = timePattern.matcher(content);
		String time = DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm");
		while(matcher.find()){
			time =  matcher.group(1);
		}
		
		matcher = answerablePattern.matcher(content);
		boolean answerable = false;
		while(matcher.find()){
			answerable = true;
		}
		
		BaiduZhidaoQuestion route = new BaiduZhidaoQuestion(pageUrl, title, qcontent, time, answerable, false);
		StoreTask stask = pool.createStoreTask(route);
		return stask;
	}
}
