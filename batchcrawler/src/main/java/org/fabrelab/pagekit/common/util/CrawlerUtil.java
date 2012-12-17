package org.fabrelab.pagekit.common.util;

import org.apache.commons.lang.StringUtils;

public class CrawlerUtil {

	public static String replaceComma(String url2) {
		if(url2==null){
			return "NULL";
		}
		String result = url2.replace(",", "，");
		return result;
	}

	public static String between(String content, String begin, String end) {
		try {
			int beginIndex = StringUtils.indexOf(content, begin)+begin.length();
			content = content.substring(beginIndex, content.length());
			int endIndex = StringUtils.indexOf(content, end);
			content = content.substring(0, endIndex);
			return content;
		} catch (Exception e) {
			return null;
		}
	}

}
