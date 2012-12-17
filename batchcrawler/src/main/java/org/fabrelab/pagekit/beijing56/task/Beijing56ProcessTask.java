package org.fabrelab.pagekit.beijing56.task;

import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.fabrelab.pagekit.beijing56.model.Beijing56Company;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.fabrelab.pagekit.common.util.CrawlerUtil;
import org.springframework.util.StringUtils;

public class Beijing56ProcessTask extends ProcessTask {

	Pattern listPattern = Pattern.compile("(/company/p_\\d+.html)");
	Pattern detailPattern = Pattern.compile("(/companyPage/.+.html)");
	
	public Beijing56ProcessTask(TaskPool pool, Page page) {
		super(pool, page);
	}
	
	public List<StoreTask> process() {
		System.out.println(Thread.currentThread().getId() + ": Processing " + page.getUrl());
		String content = fetch("utf-8");
		List<StoreTask> result = new Vector<StoreTask>();
		if(page.getUrl().contains("companyPage")){
			StoreTask stask = buildCompanyStore(content);
			if(stask!=null){
				result.add(stask);
			}
		}
		
		Matcher detailMatcher = detailPattern.matcher(content);
		while(detailMatcher.find()){
			String detailURL = "http://www.56beijing.org"+StringEscapeUtils.unescapeHtml(detailMatcher.group(1));
			Page detailPage = new Page(detailURL, Page.STATUS_WAITING, 1);
			pool.addTask(detailPage);
		}
		
		Matcher listMatcher = listPattern.matcher(content);
		while(listMatcher.find()){
			String listURL = "http://www.56beijing.org"+StringEscapeUtils.unescapeHtml(listMatcher.group(1));
			Page listPage = new Page(listURL, Page.STATUS_WAITING, 1);
			pool.addTask(listPage);
		}
		
		return result;
	}

	Pattern contactPattern = Pattern.compile("<p>[^<]+联系人：([^<]+)</p>");
	Pattern phonePattern = Pattern.compile("<p>[^<]+电话：([^<]+)</p>");
	Pattern mobilePattern = Pattern.compile("<p>[^<]+手机：([^<]+)</p>");
	Pattern addressPattern = Pattern.compile("<p>[^<]+地址：([^<]+)</p>");
	Pattern emailPattern = Pattern.compile("<p>[^<]+邮箱：([^<]+)</p>");
	
	private StoreTask buildCompanyStore(String content) {
		String title = CrawlerUtil.between(content, "<title>", "_北京物流公共信息平台");
		title = StringUtils.trimAllWhitespace(title);
		Matcher matcher = contactPattern.matcher(content);
		String contact = "";
		while(matcher.find()){
			contact =  StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		matcher = phonePattern.matcher(content);
		String phone = "";
		while(matcher.find()){
			phone =  StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		matcher = mobilePattern.matcher(content);
		String mobile = "";
		while(matcher.find()){
			 mobile =  StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		matcher = emailPattern.matcher(content);
		String email = "";
		while(matcher.find()){
			email =  StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		matcher = addressPattern.matcher(content);
		String address = "";
		while(matcher.find()){
			address =  StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		String text = CrawlerUtil.between(content, "公司特点</div>", "公司简介</div>");
		String advertise = CrawlerUtil.between(text, "<div class=\"bd\">", "</div>");
		advertise = StringUtils.trimAllWhitespace(advertise);
		text = CrawlerUtil.between(content, "公司简介</div>", "物流专线</div>");
		String description = CrawlerUtil.between(text, "<div class=\"bd\">", "</div>");
		description = StringUtils.trimAllWhitespace(description);
		Beijing56Company route = new Beijing56Company(page.getUrl(), title, contact, phone, mobile, email, address, advertise, description);
		StoreTask stask = pool.createStoreTask(route);
		return stask;
	}
}
