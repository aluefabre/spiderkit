package org.fabrelab.pagekit;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		URLHelper helper = new URLHelper(3, 3);
		String url = "http://www.newsmth.net/";
		String encoding = "GBK";
		String page = helper.getUrl(url, encoding);
		System.out.println(page);
	}

}
