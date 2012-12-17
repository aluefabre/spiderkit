package org.fabrelab.pagekit.taobaoshop;

import java.util.ArrayList;
import java.util.List;

import org.fabrelab.pagekit.common.TaskExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TaobaoShopCrawler {
	public static ApplicationContext applicationContext;
	
	public static void main(String[] argv){
//		if(argv.length!=1){
//			System.out.println("Please enter a url");
//		}
		applicationContext = new ClassPathXmlApplicationContext("spring-taobaoshop.xml");
		TaskExecutor taskExecutor = (TaskExecutor)applicationContext.getBean("taskExecutor");
		
		String seedUrl = "http://shopsearch.taobao.com/browse/shop_search.htm?sort=ratesum_desc&loc=%D5%E3%BD%AD&q=%BC%D2%BE%D3&jumpto=50&s=1960&n=40";
		List<String> seedUrls = new ArrayList<String>();
		seedUrls.add(seedUrl);
		taskExecutor.getPageDAO().seed(seedUrls);
		
		taskExecutor.start();
	}


}
