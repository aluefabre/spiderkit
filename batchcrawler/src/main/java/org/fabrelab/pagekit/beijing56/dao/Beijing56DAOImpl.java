package org.fabrelab.pagekit.beijing56.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.fabrelab.pagekit.beijing56.model.Beijing56Company;
import org.fabrelab.pagekit.common.dao.EntityDAO;
import org.fabrelab.pagekit.common.model.Entity;

public class Beijing56DAOImpl implements EntityDAO {

	public static AtomicLong idseq = new AtomicLong(1L);
	
	public static HashMap<Long, Beijing56Company> values = new HashMap<Long, Beijing56Company>();
	
	@Override
	public Long create(Entity entity) {
		Beijing56Company route = (Beijing56Company)entity;
		route.setId(idseq.getAndIncrement());
		values.put(route.getId(), route);
		System.out.println("Beijing56Company Count " + values.size());
		
		return route.getId(); 
	}

	public List<Beijing56Company> listBeijing56Companys() {
		List<Beijing56Company> result = new ArrayList<Beijing56Company>();
		result.addAll(values.values());
		return result;
	}


	@Override
	public void harvest() {
		List<Beijing56Company> routes = listBeijing56Companys();
		System.out.println("Total Route Count: " + routes.size());
		System.out.println("Saving as CSV file ... ");
		
		try {
			File csvFile = new File("beijing56_" + System.currentTimeMillis() + ".csv");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile)), true);
			for(Beijing56Company route : routes){
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
		if(values.size()>=1000){
			return true;
		}
		return false;
	}

}
