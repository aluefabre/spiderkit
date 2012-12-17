package org.fabrelab.pagekit.taobaoshop.task;

import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.fabrelab.pagekit.taobaoshop.model.TaobaoShop;
import org.springframework.util.StringUtils;

public class TaobaoShopProcessTask extends ProcessTask {

	Pattern listPattern = Pattern.compile("(http://.*shop_search.htm\\?sort=ratesum_desc[^\\\"]+s=(\\d+)[^\\\"]+)\">");
	
	public TaobaoShopProcessTask(TaskPool pool, Page page) {
		super(pool, page);
	}
	
	public List<StoreTask> process() {
		System.out.println(Thread.currentThread().getId() + ": Processing " + page.getUrl());
		String content = fetch("gbk");
		List<StoreTask> result = new Vector<StoreTask>();
		if(page.getUrl().contains("browse/shop_search.htm?sort=ratesum_desc")){
			List<StoreTask> stasks = buildTaobaoShopStore(content);
			if(stasks!=null){
				result.addAll(stasks);
			}
		}
		
		Matcher listMatcher = listPattern.matcher(content);
		while(listMatcher.find()){
			String listURL = StringEscapeUtils.unescapeHtml(listMatcher.group(1));
			String itemNo = listMatcher.group(2);
			if(itemNo.length()>3){
				Page listPage = new Page(listURL, Page.STATUS_WAITING, 1);
				pool.addTask(listPage);
			}
		}
		
		return result;
	}

	
	private List<StoreTask> buildTaobaoShopStore(String content) {
		content = content.replaceAll("<em class=\"h\">", "");
		content = content.replaceAll("</em>", "");
		
		String[] shops = content.split("<td class=\"thumb\">");
		List<StoreTask> result = new Vector<StoreTask>();
		for(String shopStr : shops){
			StoreTask stask = buildTaobaoShopStoreSingle(shopStr);
			if(stask!=null){
				result.add(stask);
			}
		}
		return result;
	}


	Pattern titlePattern = Pattern.compile("http://shop\\d+.taobao.com\" class=\"title\" target=\"_blank\">(.+)</a>");
	Pattern wangwangPattern = Pattern.compile("\"J_WangWang\" data-nick=\"([^\"]+)\"");
	Pattern addressPattern = Pattern.compile("<p>([^<]+)</p>");
	Pattern rankPattern = Pattern.compile("data-rank=\"(\\d+)\"");
	Pattern shopUrlPattern = Pattern.compile("(http://shop\\d+.taobao.com)");
	
	private StoreTask buildTaobaoShopStoreSingle(String content) {
		Matcher matcher = titlePattern.matcher(content);
		String title = "";
		while(matcher.find()){
			title =  StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		matcher = wangwangPattern.matcher(content);
		String wangwang = "";
		while(matcher.find()){
			wangwang =  StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		
		matcher = addressPattern.matcher(content);
		String address = "";
		while(matcher.find()){
			address =  StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		matcher = rankPattern.matcher(content);
		String rank = "";
		while(matcher.find()){
			rank =  StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		matcher = shopUrlPattern.matcher(content);
		String shopUrl = "";
		while(matcher.find()){
			shopUrl = StringUtils.trimAllWhitespace(matcher.group(1));
		}
		
		if(rank.length()>0){
			TaobaoShop route = new TaobaoShop(shopUrl, title, wangwang, address, rank);
			StoreTask stask = pool.createStoreTask(route);
			return stask;
		}else{
			return null;
		}
	}
}
