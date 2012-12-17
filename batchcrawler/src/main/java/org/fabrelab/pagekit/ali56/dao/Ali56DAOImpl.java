package org.fabrelab.pagekit.ali56.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.impl.cookie.DateUtils;
import org.fabrelab.pagekit.ali56.model.Ali56Entity;
import org.fabrelab.pagekit.common.dao.EntityDAO;
import org.fabrelab.pagekit.common.model.Entity;

public class Ali56DAOImpl implements EntityDAO {


	public static AtomicLong idseq = new AtomicLong(1L);
	
	public static HashMap<Long, Ali56Entity> values = new HashMap<Long, Ali56Entity>();
	
	int maxCount = 200;

	int batchSize = 200;
	
	String fileName = "";
	
	static AtomicInteger index = new AtomicInteger(1972);
	
	@Override
	public Long create(Entity entity) {
		Ali56Entity route = (Ali56Entity)entity;
		route.setId(idseq.getAndIncrement());
		values.put(route.getId(), route);
		System.out.println("Ali56Entity Count " + values.size());
		
		if(values.size()>=batchSize){
			harvest();
			values.clear();
		}
		
		return route.getId(); 
	}

	public List<Ali56Entity> list() {
		List<Ali56Entity> result = new ArrayList<Ali56Entity>();
		result.addAll(values.values());
		return result;
	}


	@Override
	public void harvest() {
		List<Ali56Entity> routes = list();
		System.out.println("Total Route Count: " + routes.size());
		System.out.println("Saving as Sitemap file ... ");
		
		List<Ali56Entity> batch = new ArrayList<Ali56Entity>();
		for(Ali56Entity route : routes){
			batch.add(route);
			if(batch.size()>=batchSize){
				printDataAsHtml(batch);
				batch.clear();
			}
		}
		if(!batch.isEmpty()){
			printDataAsHtml(batch);
		}
	}

	private void printDataAsHtml(List<Ali56Entity> routes) {
		try {
			File csvFile = new File("sitemap_" + fileName + "_" + index.getAndIncrement() + ".xml");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile)), true);
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<urlset>");
					
			for(Ali56Entity route : routes){
				out.println("  <url>");
				out.println("    <loc>"+route.getUrl()+"</loc>");
				out.println("    <lastmod>"+DateUtils.formatDate(new Date(),"yyyy-MM-dd")+"</lastmod>");
				out.println("    <changefreq>daily</changefreq>");
				out.println("    <priority>1.0</priority>");
				out.println("  </url>");
			}
			out.println("</urlset>");

			out.flush();
			out.close();
			
			System.out.println("Saving as HTML file ... DONE! fileName: " + csvFile.getName());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Saving as HTML file ... FAILED! ");
		}
	}

	@Override
	public boolean enough() {
		if(values.size()>=maxCount){
			return true;
		}
		return false;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
