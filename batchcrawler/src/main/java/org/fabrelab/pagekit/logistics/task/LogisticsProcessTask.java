package org.fabrelab.pagekit.logistics.task;

import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.fabrelab.pagekit.logistics.model.Route;

public class LogisticsProcessTask extends ProcessTask {

	Pattern listPattern = Pattern.compile("(http://.*athena/offerlist.*html)");
	Pattern shopPattern = Pattern.compile("(http://.*buyer/offerdetail.*html)");
	Pattern list2Pattern = Pattern.compile("(http://.*selloffer/--1033594.html.*beginPage=\\d+)");
	
	public LogisticsProcessTask(TaskPool pool, Page page) {
		super(pool, page);
	}
	
	public List<StoreTask> process() {
		System.out.println(Thread.currentThread().getId() + ": Processing " + page.getUrl());
		String content = fetch("gbk");
		List<StoreTask> stasks = new Vector<StoreTask>();
		if(page.getUrl().contains("buyer/offerdetail/")){
			StoreTask stask = buildDianpingStore(content);
			if(stask!=null){
				stasks.add(stask);
			}
		}else{
			Matcher listMatcher = listPattern.matcher(content);
			while(listMatcher.find()){
				String listURL = listMatcher.group(1);
				Page listPage = new Page(listURL, Page.STATUS_WAITING, 1);
				pool.addTask(listPage);
			}
			Matcher list2Matcher = list2Pattern.matcher(content);
			while(list2Matcher.find()){
				String listURL = list2Matcher.group(1);
				Page listPage = new Page(listURL, Page.STATUS_WAITING, 1);
				pool.addTask(listPage);
			}
			Matcher matcher = shopPattern.matcher(content);
			while(matcher.find()){
				String shopURL = matcher.group(1);
				Page shopPage = new Page(shopURL, Page.STATUS_WAITING, 2);
				pool.addTask(shopPage);
			}
		}
		return stasks;
	}

	Pattern titlePattern = Pattern.compile("<h1>(.+)</h1>");
	Pattern pricePattern = Pattern.compile("<span class=\"de-pnum\">([^<]+)</span>");
	Pattern priceUnitPattern = Pattern.compile("<span class=\"de-unit\">([^<]+)</span>");
	Pattern startPointPattern = Pattern.compile("<em class=\"de-line-r\">(.+)</em>");
	
	String tableStart = "<div id=\"mod-detail-attributes\" class=\"mod-detail-attributes\">";
	
	Pattern tableDataPattern = Pattern.compile("<td>(.+)</td>");
	
	String categoryFeature = "c-1033594_n-y.html\">国内陆运</a></li>";
	
	private StoreTask buildDianpingStore(String content) {
		
		if(!content.contains(categoryFeature)){
			return null;
		}
		
		Matcher matcher = titlePattern.matcher(content);
		String title = "";
		while(matcher.find()){
			title = matcher.group(1);
		}
		
		matcher = pricePattern.matcher(content);
		String price = "";
		while(matcher.find()){
			price = matcher.group(1);
		}
		
		
		matcher = priceUnitPattern.matcher(content);
		String priceUnit = "";
		while(matcher.find()){
			priceUnit = matcher.group(1);
		}
		
		matcher = startPointPattern.matcher(content);
		String startPoint = "";
		while(matcher.find()){
			startPoint = matcher.group(1);
		}
		
		int tableStartIndex = content.indexOf(tableStart);
		
		content = content.substring(tableStartIndex);
		
		int tableEndIndex = content.indexOf("</div>");
		
		content = content.substring(0, tableEndIndex);
		
		matcher = tableDataPattern.matcher(content);
	
		String[] tableDatas = new String[6];
		int i=0;
		while(matcher.find()){
			if(i<6){
				tableDatas[i] = matcher.group(1);
				i++;
			}
		}
		
		Route route = new Route(page.getUrl(), title, price, priceUnit, startPoint, 
				tableDatas[0], tableDatas[1],tableDatas[2],tableDatas[3],tableDatas[4],tableDatas[5]);
		
		StoreTask stask = pool.createStoreTask(route);
		return stask;
	}

	
	
}
