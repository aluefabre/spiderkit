package org.fabrelab.pagekit.baiduzhidao.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.fabrelab.pagekit.baiduzhidao.model.BaiduZhidaoQuestion;
import org.fabrelab.pagekit.common.dao.EntityDAO;
import org.fabrelab.pagekit.common.model.Entity;

public class BaiduZhidaoDAOImpl implements EntityDAO {

	public static AtomicLong idseq = new AtomicLong(1L);
	
	public static HashMap<Long, BaiduZhidaoQuestion> values = new HashMap<Long, BaiduZhidaoQuestion>();
	
	int maxCount = 100;
	
	@Override
	public Long create(Entity entity) {
		BaiduZhidaoQuestion route = (BaiduZhidaoQuestion)entity;
		route.setId(idseq.getAndIncrement());
		values.put(route.getId(), route);
		System.out.println("BaiduZhidaoQuestion Count " + values.size());
		
		return route.getId(); 
	}

	public List<BaiduZhidaoQuestion> list() {
		List<BaiduZhidaoQuestion> result = new ArrayList<BaiduZhidaoQuestion>();
		result.addAll(values.values());
		return result;
	}


	@Override
	public void harvest() {
		List<BaiduZhidaoQuestion> routes = list();
		System.out.println("Total Route Count: " + routes.size());
		System.out.println("Saving as HTML file ... ");
		
		List<BaiduZhidaoQuestion> batch = new ArrayList<BaiduZhidaoQuestion>();
		for(BaiduZhidaoQuestion route : routes){
			batch.add(route);
			if(batch.size()>=200){
				printDataAsHtml(batch);
				batch.clear();
			}
		}
	}

	private void printDataAsHtml(List<BaiduZhidaoQuestion> routes) {
		try {
			File csvFile = new File("shop_" + System.currentTimeMillis() + ".htm");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile)), true);
			out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
					"<head>" +
					"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\"> " +
					"</head>" +
					"<body>" +
					"<table>");
					
			for(BaiduZhidaoQuestion route : routes){
				out.println("<tr>");
				out.println(route.toCsvString());
				out.println("</tr>");
			}
			out.println("</table>" +
					"<body>" +
					"<html> " +
					"</head>");

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

}
