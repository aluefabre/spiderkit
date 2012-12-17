package org.fabrelab.pagekit.logistics.dao;

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
import org.fabrelab.pagekit.logistics.model.Route;

public class RouteDAOImpl implements EntityDAO{

	public static AtomicLong idseq = new AtomicLong(1L);
	
	public static HashMap<Long, Route> routes = new HashMap<Long, Route>();
	
	@Override
	public Long create(Entity entity) {
		Route route = (Route)entity;
		route.setId(idseq.getAndIncrement());
		routes.put(route.getId(), route);
		System.out.println("Route Count " + routes.size());

		return route.getId(); 
	}

	public List<Route> listRoutes() {
		List<Route> result = new ArrayList<Route>();
		result.addAll(routes.values());
		return result;
	}

	@Override
	public void harvest() {
		List<Route> routes = listRoutes();
		System.out.println("Total Route Count: " + routes.size());
		System.out.println("Saving as CSV file ... ");
		
		try {
			File csvFile = new File("routes_" + System.currentTimeMillis() + ".csv");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile)), true);
			for(Route route : routes){
				out.println(route.toCsvString());
			}
			out.flush();
			out.close();
			
			System.out.println("Saving as CSV file ... DONE! fileName: " + csvFile.getName());
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Saving as CSV file ... FAILED! ");
	}

	@Override
	public boolean enough() {
		if(routes.size()>=3000){
			return true;
		}
		return false;
	}

}
