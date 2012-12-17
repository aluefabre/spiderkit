package org.fabrelab.pagekit.baiduzhidao;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.fabrelab.pagekit.CrawlerException;
import org.fabrelab.pagekit.baiduzhidao.dao.BaiduZhidaoDAOImpl;
import org.fabrelab.pagekit.common.TaskExecutor;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaiduZhidaoCrawler {
	private static ClassPathXmlApplicationContext applicationContext;

	public static boolean started = false;
	
	public static boolean run(String word, QuestionProcessor processor, int maxCount) throws CrawlerException {
		if(started){
			try {
				TaskExecutor taskExecutor = (TaskExecutor)applicationContext.getBean("taskExecutor");
				taskExecutor.stop();
				applicationContext.close();
				applicationContext = null;
			} catch (Exception e) {
				throw new CrawlerException(e);
			}
		}else{
			try {
				applicationContext = new ClassPathXmlApplicationContext("spring-zhidao.xml");
				String encoded = URLEncoder.encode(word, "GBK");
				TaskExecutor taskExecutor = (TaskExecutor)applicationContext.getBean("taskExecutor");
				BaiduZhidaoDAOImpl baiduZhidaoDAO = (BaiduZhidaoDAOImpl)applicationContext.getBean("entityDAO");

				baiduZhidaoDAO.setMaxCount(maxCount);

				BaiduZhidaoTaskPool pool = (BaiduZhidaoTaskPool)taskExecutor.getTaskPool();
				pool.setWord(word);

				pool.setProcessor(processor);
				
				String seedUrl = "http://zhidao.baidu.com/q?ct=17&tn=ikaslist&rn=10&word="+encoded +"&lm=65536&pn=0";
				List<String> seedUrls = new ArrayList<String>();
				seedUrls.add(seedUrl);
				taskExecutor.getPageDAO().seed(seedUrls);
				taskExecutor.start();
			} catch (Exception e) {
				throw new CrawlerException(e);
			}
		}
		started = !started;
		return started;
	}

	public static boolean pause() throws CrawlerException {
		if(started){
			try {
				TaskExecutor taskExecutor = (TaskExecutor)applicationContext.getBean("taskExecutor");
				return taskExecutor.pause();
			} catch (Exception e) {
				throw new CrawlerException(e);
			}
		}
		return false;
	}
	
	public static void stop() throws CrawlerException {
		if(started){
			try {
				TaskExecutor taskExecutor = (TaskExecutor)applicationContext.getBean("taskExecutor");
				taskExecutor.stop();
				applicationContext.close();
				applicationContext = null;
			} catch (Exception e) {
				throw new CrawlerException(e);
			}
		}
		started = false;
	}
	
	
	public static void main(String[] argv) throws Exception{
		
		String word = "物流";
		BaiduZhidaoCrawler.run(word, null, 1500);
		
	}

}
