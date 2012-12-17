package org.fabrelab.pagekit.ali56.model;

import org.fabrelab.pagekit.common.model.Entity;

public class Ali56Entity implements Entity {
	Long id;
	String url;
	String entityCode;
	String corpCode;
	
	public Ali56Entity(String corpCode, String entityCode, String url) {
		super();
		this.corpCode = corpCode;
		this.entityCode = entityCode;
		this.url = url;
	}

	public String toPrintString(){
		String Split = " ";
		return this.corpCode + Split + this.entityCode + Split;
	}
	
	public String toCsvString(){

		String Split = ",";
		return this.corpCode + Split + this.entityCode  ;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
