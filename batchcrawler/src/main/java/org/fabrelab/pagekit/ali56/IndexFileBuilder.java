package org.fabrelab.pagekit.ali56;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.apache.http.impl.cookie.DateUtils;
import org.fabrelab.pagekit.ali56.dao.Ali56DAOImpl;
import org.fabrelab.pagekit.ali56.model.Ali56Entity;

public class IndexFileBuilder {
	public static void main(String[] args) throws IOException {
		Ali56DAOImpl dao = new Ali56DAOImpl();
		dao.setFileName("index");
		File dir = new File("D:\\workspace\\java\\google-workspace\\pagekit.crawler");
		String[] list = dir.list();
		for (String string : list) {
			if(string.startsWith("sitemap_")){
				addToDao(dao, Ali56AreaUrlBuilder.ALI56DOMAIN + "sitemap1/"+string);
			}
		}
		printDataAsHtml(dao.list());
		printDataAsList(dao.list());
	}
	
	private static void printDataAsList(List<Ali56Entity> routes) {
		try {
			File csvFile = new File("sitemap_list.txt");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile)), true);
					
			for(Ali56Entity route : routes){
				out.println(route.getUrl());
			}

			out.flush();
			out.close();
			
			System.out.println("Saving as HTML file ... DONE! fileName: " + csvFile.getName());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Saving as HTML file ... FAILED! ");
		}
	}

	private static void printDataAsHtml(List<Ali56Entity> routes) {
		try {
			File csvFile = new File("sitemap_index1.xml");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile)), true);
			out.println("<sitemapindex>");
					
			for(Ali56Entity route : routes){
				out.println("  <sitemap>");
				out.println("    <loc>"+route.getUrl()+"</loc>");
				out.println("    <lastmod>"+DateUtils.formatDate(new Date(),"yyyy-MM-dd")+"</lastmod>");
				out.println("  </sitemap>");
			}
			out.println("</sitemapindex>");

			out.flush();
			out.close();
			
			System.out.println("Saving as HTML file ... DONE! fileName: " + csvFile.getName());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Saving as HTML file ... FAILED! ");
		}
	}
	
	private static void addToDao(Ali56DAOImpl dao, String url) {
		dao.create(new Ali56Entity("","",url));
	}
}
