package org.fabrelab.pagekit.logistics.model;

import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.util.CrawlerUtil;

public class Route implements Entity {
	Long id;
	String url;
	String title;
	String price;
	String priceUnit;
	String startPoint;
	String startLocation; 
	String targetLocation; 
	String isDedicatedLine;
	String transportType; 
	String lastLoadDate; 
	String cargoType;
			
	public Route(String url2, String title2, String price, String priceUnit, String startPoint,
			String startLocation, String targetLocation, String isDedicatedLine,
			String transportType, String lastLoadDate, String cargoType) {
		this.url = CrawlerUtil.replaceComma(url2);
		this.title = CrawlerUtil.replaceComma(title2);
		this.price = CrawlerUtil.replaceComma(price);
		this.priceUnit = CrawlerUtil.replaceComma(priceUnit);
		this.startPoint = CrawlerUtil.replaceComma(startPoint);
		this.startLocation = CrawlerUtil.replaceComma(startLocation);
		this.targetLocation = CrawlerUtil.replaceComma(targetLocation);
		this.isDedicatedLine = CrawlerUtil.replaceComma(isDedicatedLine);
		this.transportType = CrawlerUtil.replaceComma(transportType);
		this.lastLoadDate = CrawlerUtil.replaceComma(lastLoadDate);
		this.cargoType = CrawlerUtil.replaceComma(cargoType);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	public String toPrintString(){

		String Split = " \n";
		return 
				this.url + Split +
				"Title:          " + this.title + Split +
				"Price:          " + this.price + Split +
				"priceUnit:      " + this.priceUnit + Split +
				"startPoint:     " + this.startPoint + Split +
				"startLocation:  "  + this.startLocation + Split +
				"targetLocation: " + this.targetLocation + Split +
				"isDedicatedLine:" + this.isDedicatedLine + Split +
				"transportType:  " + this.transportType + Split +
				"lastLoadDate:   " + this.lastLoadDate + Split +
				"cargoType:      " + this.cargoType + Split;
	}
	
	public String toCsvString(){

		String Split = ", ";
		return 
				this.url + Split +
				this.title + Split +
				this.price + Split +
				this.priceUnit + Split +
				this.startPoint + Split +
				this.startLocation + Split +
				this.startLocation.length() + Split +
				this.targetLocation + Split +
				this.targetLocation.length() + Split +
				this.isDedicatedLine + Split +
				this.transportType + Split +
				this.lastLoadDate + Split +
				this.cargoType + Split;
	}
}
