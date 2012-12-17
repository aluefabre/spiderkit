package org.fabrelab.pagekit.common.model;

public class Page {
	public static final String STATUS_WAITING = "waiting";
	public static final String STATUS_RUNNING = "running";
	public static final String STATUS_DONE = "done";
	public static final String STATUS_ERROR = "error";
	
	Long id;
	String url;
	String status;
	Integer priority;
	
	public Page(){
		
	}

	public Page(String url, String status, int priority) {
		this.url = url;
		this.status = status;
		this.priority = priority;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
}
