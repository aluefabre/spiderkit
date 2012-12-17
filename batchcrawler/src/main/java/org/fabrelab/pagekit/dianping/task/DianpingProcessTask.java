package org.fabrelab.pagekit.dianping.task;

import java.net.URLEncoder;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fabrelab.pagekit.URLHelper;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.fabrelab.pagekit.dianping.model.Shop;

public class DianpingProcessTask extends ProcessTask {

	Pattern listPattern = Pattern.compile("href=\\\"(/search/category[^\\s]+)\\\"");
	Pattern shopPattern = Pattern.compile("href=\\\"(/shop/\\d+)");
	Pattern titlePattern = Pattern.compile("<h1 class=\\\"shop-title\\\" .+>(.+)</h1>");
	Pattern addressPattern = Pattern.compile("<span class=\\\"region\\\".+>(.+)</span></a><span itemprop=\\\"street-address\\\">([^<]+)</span>");
	Pattern coordinatePattern = Pattern.compile("var viewport_center_lat=([\\d.]+);var viewport_center_lng=([\\d.]+)");
	
	public DianpingProcessTask(TaskPool pool, Page page) {
		super(pool, page);
	}
	
	public List<StoreTask> process() {
		System.out.println(Thread.currentThread().getId() + ": Processing " + page.getUrl());
		String content = fetch("gbk");
		List<StoreTask> stasks = new Vector<StoreTask>();
		if(page.getUrl().contains("/shop/")){
			StoreTask stask = buildDianpingStore(content);
			stasks.add(stask);
		}else{
			Matcher listMatcher = listPattern.matcher(content);
			while(listMatcher.find()){
				String listURL = listMatcher.group(1);
				Page listPage = new Page("http://www.dianping.com"+listURL, Page.STATUS_WAITING, 1);
				pool.addTask(listPage);
			}
			
			Matcher matcher = shopPattern.matcher(content);
			while(matcher.find()){
				String shopURL = matcher.group(1);
				Page shopPage = new Page("http://www.dianping.com" + shopURL, Page.STATUS_WAITING, 2);
				pool.addTask(shopPage);
			}
		}
		return stasks;
	}

	private StoreTask buildDianpingStore(String content) {
		Matcher matcher = titlePattern.matcher(content);
		String title = "";
		while(matcher.find()){
			title = matcher.group(1);
		}
		
		matcher = addressPattern.matcher(content);
		String address = "";
		while(matcher.find()){
			address = matcher.group(1)+matcher.group(2);
		}
		
		String[] coordinates = getCoordinates(address);
		
		Shop shop = new Shop(page.getUrl(), title, address, coordinates[0], coordinates[1]);
		
		StoreTask stask = pool.createStoreTask(shop);
		return stask;
	}

	
	private String[] getCoordinates(String address) {
		try {
			String gmapUrl = "http://ditu.google.cn/maps?daddr="+URLEncoder.encode(address, "utf-8");
			URLHelper helper = new URLHelper(3,3);
			String gmapContent = helper.getUrl(gmapUrl, "gbk");
			Matcher matcher = coordinatePattern.matcher(gmapContent);

			while(matcher.find()){
				return new String[]{matcher.group(1), matcher.group(2)};
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{"", ""};
	}
	
}
