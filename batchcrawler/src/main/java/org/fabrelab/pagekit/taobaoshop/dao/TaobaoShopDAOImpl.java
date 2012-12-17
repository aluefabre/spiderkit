package org.fabrelab.pagekit.taobaoshop.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.fabrelab.pagekit.common.dao.EntityDAO;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.taobaoshop.model.TaobaoShop;

public class TaobaoShopDAOImpl implements EntityDAO {

	public static AtomicLong idseq = new AtomicLong(1L);
	
	public static HashMap<Long, TaobaoShop> values = new HashMap<Long, TaobaoShop>();
	
	@Override
	public Long create(Entity entity) {
		TaobaoShop route = (TaobaoShop)entity;
		route.setId(idseq.getAndIncrement());
		values.put(route.getId(), route);
		System.out.println("TaobaoShop Count " + values.size());
		
		return route.getId(); 
	}

	public List<TaobaoShop> listTaobaoShops() {
		List<TaobaoShop> result = new ArrayList<TaobaoShop>();
		result.addAll(values.values());
		return result;
	}


	@Override
	public void harvest() {
		List<TaobaoShop> routes = listTaobaoShops();
		System.out.println("Total Route Count: " + routes.size());
		System.out.println("Saving as HTML file ... ");
		
		List<TaobaoShop> batch = new ArrayList<TaobaoShop>();
		for(TaobaoShop route : routes){
			batch.add(route);
			if(batch.size()>=200){
				printDataAsHtml(batch);
				batch.clear();
			}
		}
	}

	private void printDataAsHtml(List<TaobaoShop> routes) {
		try {
			File csvFile = new File("shop_" + System.currentTimeMillis() + ".htm");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile)), true);
			out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
					"<head>" +
					"<meta content=\"text/html; charset=UTF-8\" http-equiv=\"Content-Type\"> " +
					"</head>" +
					"<body>" +
					"<table>");
					
			for(TaobaoShop route : routes){
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
		if(values.size()>=100){
			return true;
		}
		return false;
	}

}
