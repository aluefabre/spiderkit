package org.fabrelab.pagekit.logistics;

import java.util.ArrayList;
import java.util.List;

import org.fabrelab.pagekit.common.TaskExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LogisticsCrawler {
	public static ApplicationContext applicationContext;

	public static void main(String[] argv){
//		if(argv.length!=1){
//			System.out.println("Please enter a url");
//		}
		applicationContext = new ClassPathXmlApplicationContext("spring-logistics.xml");
		TaskExecutor taskExecutor = (TaskExecutor)applicationContext.getBean("taskExecutor");
		
		String seedUrl = "http://search.china.alibaba.com/selloffer/-CEEFC1F7-1033594.html";
		List<String> seedUrls = new ArrayList<String>();
		seedUrls.add(seedUrl);
		taskExecutor.getPageDAO().seed(seedUrls);
		
		taskExecutor.start();
	}

}
