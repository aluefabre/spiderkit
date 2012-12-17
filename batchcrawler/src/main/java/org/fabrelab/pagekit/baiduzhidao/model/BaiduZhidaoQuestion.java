package org.fabrelab.pagekit.baiduzhidao.model;

import org.fabrelab.pagekit.common.model.Entity;

public class BaiduZhidaoQuestion implements Entity {
	Long id;
	String url;
	String title;
	String content;
	String time;
	boolean answerable;
	boolean answered;
	
	String type;
	String extractedText;
	String analysisResult;
	
	String answer;
	
	public BaiduZhidaoQuestion(String url, String title, String content, String time,
			boolean answerable, boolean answered) {
		super();
		this.url = url;
		this.title = title;
		this.content = content;
		this.time = time;
		this.answerable = answerable;
		this.answered = answered;
	}

	public String toPrintString(){

		String Split = " \n";
		return 
				this.url + Split +
				"Title:          " + this.title + Split +
				"content:          " + this.content + Split +
				"time:      " + this.time + Split +
				"answerable:     " + this.answerable + Split +
				"answered:     " + this.answered + Split ;
	}
	
	public String toCsvString(){

		String Split = "&nbsp;&nbsp;&nbsp;</td><td>";
		return "<td>" + 
				this.url + Split +
				this.title + Split +
				this.content + Split +
				this.time + Split +
				this.answerable + Split +
				this.answered + "</td>" ;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isAnswerable() {
		return answerable;
	}

	public void setAnswerable(boolean answerable) {
		this.answerable = answerable;
	}

	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAnalysisResult() {
		return analysisResult;
	}

	public void setAnalysisResult(String analysisResult) {
		this.analysisResult = analysisResult;
	}

	public String getExtractedText() {
		return extractedText;
	}

	public void setExtractedText(String extractedText) {
		this.extractedText = extractedText;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}


}
