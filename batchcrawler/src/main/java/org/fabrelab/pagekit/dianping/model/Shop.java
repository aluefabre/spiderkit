package org.fabrelab.pagekit.dianping.model;

import org.fabrelab.pagekit.common.model.Entity;

public class Shop implements Entity {
	Long id;
	
	String title;
	
	String address;
	
	String latitude;
	
	String longitude;

	String url;
	
	public Shop(){
		
	}
	
	public Shop(String url, String title, String address, String latitude, String longitude) {
		this.url = url;
		this.title = title;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
