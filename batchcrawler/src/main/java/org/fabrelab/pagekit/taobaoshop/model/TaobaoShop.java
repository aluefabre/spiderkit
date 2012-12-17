package org.fabrelab.pagekit.taobaoshop.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.util.CrawlerUtil;

public class TaobaoShop implements Entity {
	Long id;
	String url;
	String title;
	String wangWang;
	String address;
	String rank;
			
	public TaobaoShop(String url2, String title2, String wangWang2, String address2, String rank2) {
		this.url = CrawlerUtil.replaceComma(url2);
		this.title = CrawlerUtil.replaceComma(title2);
		this.wangWang = CrawlerUtil.replaceComma(wangWang2);
		this.address = CrawlerUtil.replaceComma(address2);
		this.rank = CrawlerUtil.replaceComma(rank2);
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
				"WangWang:          " + this.wangWang + Split +
				"Address:      " + this.address + Split +
				"Rank:     " + this.rank + Split ;
	}
	
	public String toCsvString(){

		String Split = "&nbsp;&nbsp;&nbsp;</td><td>";
		return "<td>" + 
				this.url + Split +
				this.title + Split +
				this.wangWang + Split +
				createWangwangShow() + Split +
				this.address + Split +
				this.rank + "</td>" ;
	}

	private String createWangwangShow()  {
		String result = "";
		try {
			String encodedStr = URLEncoder.encode(this.wangWang,"GBK");
			result = "<a href=\"http://amos.alicdn.com/msg.aw?v=2&uid="+encodedStr+"&site=cntaobao&s=4\" target=\"_blank\">" +
			"<img border=\"0\" alt=\"点击这里给我发消息\" src=\"http://amos.alicdn.com/online.aw?v=2&uid="+encodedStr+"&site=cntaobao&s=4\">" +
			"</a>";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
