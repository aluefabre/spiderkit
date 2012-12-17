package org.fabrelab.pagekit.ali56;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fabrelab.pagekit.URLHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Ali56Client {

	static public String getTraceByCodeAndMailNo(String code, String mailNo) {
		String serverUrl = "http://56.china.alibaba.com/order/mobile/mobileQueryTrace.do?c=" + code + "&m="
				+ mailNo;
		URLHelper helper = new URLHelper(1, 3);
		String urlResult = helper.getUrl(serverUrl, "gbk");
		String result = "";
		if (StringUtils.isNotBlank(urlResult)) {
			Gson gson = new Gson();
			java.lang.reflect.Type listType = new TypeToken<MobileTraceJsonModel>() {
			}.getType();
			MobileTraceJsonModel traceResult = gson.fromJson(urlResult, listType);
			if ("1".equals(traceResult.getStatus())) {
				for (MobileTraceStep step : traceResult.getData()) {
					result = step.getT() + " " + step.getR() + "\n" + result;
				}
			}
		}
		return result;
	}

	static public String getNetwork(String addr) {
		// TODO Auto-generated method stub
		return null;
	}

	static public String getNetwork(String code, String addr) {
		// TODO Auto-generated method stub
		return null;
	}

	static public String getRoute(String code, List<String> addresses) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getRoute(List<String> addresses) {
		// TODO Auto-generated method stub
		return null;
	}

}
