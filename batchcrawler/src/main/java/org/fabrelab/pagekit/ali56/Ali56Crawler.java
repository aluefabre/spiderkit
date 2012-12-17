package org.fabrelab.pagekit.ali56;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.fabrelab.pagekit.ali56.dao.Ali56DAOImpl;
import org.fabrelab.pagekit.common.TaskExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Ali56Crawler {
	public static ApplicationContext applicationContext;


	public static final String ALI56DOMAIN = "http://56.china.alibaba.com";
	
	public static void main(String[] argv) throws UnsupportedEncodingException{
//		if(argv.length!=1){
//			System.out.println("Please enter a url");
//		}
		applicationContext = new ClassPathXmlApplicationContext("spring-ali56.xml");

		TaskExecutor taskExecutor = (TaskExecutor)applicationContext.getBean("taskExecutor");
		Ali56DAOImpl baiduZhidaoDAO = (Ali56DAOImpl)applicationContext.getBean("entityDAO");
		baiduZhidaoDAO.setMaxCount(15000000);
		
		InputStream is = Ali56Crawler.class.getResourceAsStream("corp_all.csv");
		List<String> originalLines = TextLoader.loadText(is,"gbk");
		
		List<String> seedUrls = new ArrayList<String>();
		for (String line : originalLines) {
			String[] parts = line.split(",");

			String word = parts[0].replaceAll("\"", "").toLowerCase();
			seedUrls.add(ALI56DOMAIN+"/corp-"+word+"/search_network-p0.htm?useDefault=1");
			seedUrls.add(ALI56DOMAIN+"/corp-"+word+"/search_routes-p0.htm?useDefault=1");
		}
		
		taskExecutor.getPageDAO().seed(seedUrls);
		
		taskExecutor.start();
	}



}
