package org.fabrelab.pagekit.beijing56.model;

import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.util.CrawlerUtil;

public class Beijing56Company implements Entity {
	
	Long id;
	
	String title; 
	String contact; 
	String phone; 
	String mobile; 
	String email;
	String address; 
	String advertise; 
	String description;
	String url;
	
	public Beijing56Company(String url, String title, String contact, String phone, String mobile, String email,
			String address, String advertise, String description) {
		this.url = url;
		this.title = CrawlerUtil.replaceComma(title); 
		this.contact= CrawlerUtil.replaceComma(contact);  
		this.phone= CrawlerUtil.replaceComma(phone); 
		this.mobile= CrawlerUtil.replaceComma(mobile); 
		this.email= CrawlerUtil.replaceComma(email); 
		this.address= CrawlerUtil.replaceComma(address); 
		this.advertise= CrawlerUtil.replaceComma(advertise); 
		this.description= CrawlerUtil.replaceComma(description); 
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String toPrintString(){

		String Split = " \n";
		return 
				this.url + Split +
				this.title + Split +
				this.contact + Split +
				this.phone + Split +
				this.mobile + Split +
				this.email + Split +
				this.address + Split +
				this.advertise + Split +
				this.description + Split;
	}
	
	public String toCsvString(){

		String Split = ", ";
		return 
				this.url + Split +
				this.title + Split +
				this.contact + Split +
				this.phone + Split +
				this.mobile + Split +
				this.email + Split +
				this.address + Split +
				this.advertise + Split +
				this.description + Split;
	}

}
