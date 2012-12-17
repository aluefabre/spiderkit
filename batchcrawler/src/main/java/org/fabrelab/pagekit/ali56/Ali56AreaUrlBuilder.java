package org.fabrelab.pagekit.ali56;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fabrelab.pagekit.ali56.dao.Ali56DAOImpl;
import org.fabrelab.pagekit.ali56.model.Ali56Entity;
import org.springframework.context.ApplicationContext;

public class Ali56AreaUrlBuilder {
	public static ApplicationContext applicationContext;


	public static final String ALI56DOMAIN = "http://56.china.alibaba.com/";
	
	public static void main(String[] argv) throws UnsupportedEncodingException{

		
		List<String> areaPinyins = loadAreaCityPinyins(null);
		List<String> areaCityPinyins = loadAreaCityPinyins("CT");
		List<String> areaCountyPinyins = loadAreaCityPinyins("CN");
		createRouteFrom(areaPinyins);
		

		createRouteBetwteen("routecttoct", areaCityPinyins, areaCityPinyins);
		
		List<String> allCorpCodes = loadCorpCodes("corp_all.csv");
		createCorpPages(allCorpCodes);
		
		List<String> networkCorpCodes = loadCorpCodes("corp_network20.csv");
		createCorpNetwork(networkCorpCodes, areaPinyins);
		
		List<String> routeCorpCodes = loadCorpCodes("corp_route200.csv");
		createCorpRoute(routeCorpCodes, areaPinyins);
		
		createRouteBetwteen("routecntoct",areaCountyPinyins, areaCityPinyins);
	}

	private static void createCorpPages(List<String> corpCodes) {
		Ali56DAOImpl dao = new Ali56DAOImpl();
		dao.setFileName("corpmenu");
		for (String corpCode : corpCodes) {
			addToDao(dao, ALI56DOMAIN + "corp-"+corpCode+"/");
			addToDao(dao, ALI56DOMAIN + "corp-"+corpCode+"/introduction.htm");
			addToDao(dao, ALI56DOMAIN + "corp-"+corpCode+"/product.htm");
			addToDao(dao, ALI56DOMAIN + "corp-"+corpCode+"/contact.htm");
			addToDao(dao, ALI56DOMAIN + "corp-"+corpCode+"/search_routes.htm");
			addToDao(dao, ALI56DOMAIN + "corp-"+corpCode+"/search-network.htm");
			addToDao(dao, ALI56DOMAIN + "corp-"+corpCode+"/routes-index.htm");
			addToDao(dao, ALI56DOMAIN + "corp-"+corpCode+"/networks-index.htm");
		}
		dao.harvest();
	}

	private static void createCorpRoute(List<String> corpCodes, List<String> areaPinyins) {
		Ali56DAOImpl dao = new Ali56DAOImpl();

		dao.setFileName("corproute");
		for (String corpCode : corpCodes) {
			for (String areaPinyin : areaPinyins) {
				addToDao(dao, ALI56DOMAIN + "corp-"+corpCode+"/routes-"+areaPinyin+".htm");
			}
		}
		
		dao.harvest();
	}

	private static void createCorpNetwork(List<String> corpCodes, List<String> areaPinyins) {
		Ali56DAOImpl dao = new Ali56DAOImpl();

		dao.setFileName("corpnetwork");
		for (String corpCode : corpCodes) {
			for (String areaPinyin : areaPinyins) {
				addToDao(dao, ALI56DOMAIN + "corp-"+corpCode+"/networks-"+areaPinyin+".htm");
			}
		}

		dao.harvest();
	}

	private static void createRouteBetwteen(String filename, List<String> areaPinyins, List<String> areaPinyins2) {
		Ali56DAOImpl dao = new Ali56DAOImpl();

		dao.setFileName(filename);
		for (String areaPinyin : areaPinyins) {
			for (String areaPinyin2 : areaPinyins2) {
				addToDao(dao, ALI56DOMAIN + "routes/"+areaPinyin+"-"+areaPinyin2+".htm");
			}
		}
		dao.harvest();
	}

	private static void createRouteFrom(List<String> areaPinyins) {
		Ali56DAOImpl dao = new Ali56DAOImpl();

		dao.setFileName("routefrom");
		addToDao(dao, ALI56DOMAIN + "routes/area-index.htm");
		for (String areaPinyin : areaPinyins) {
			addToDao(dao, ALI56DOMAIN + "routes/"+areaPinyin+".htm");
		}

		dao.harvest();
	}

	private static List<String> loadAreaCityPinyins(String loadType) {
		InputStream is = Ali56AreaUrlBuilder.class.getResourceAsStream("area_all.csv");
		List<String> originalLines = TextLoader.loadText(is,"gbk");
		
		List<String> areaPinyins = new ArrayList<String>();

		List<String> areaCodes = new ArrayList<String>();
		
		List<String> types = new ArrayList<String>();
		
		for (String line : originalLines) {
			String[] parts = line.split(",");

		
			String type = parts[0].replaceAll("\"", "");
			if("CT".equals(type)||"CN".equals(type)){
				String word = parts[3].replaceAll("\"", "").toLowerCase();
				word = cutTail(word);
				areaPinyins.add(word);
				areaCodes.add(parts[2].replaceAll("\"", ""));
				types.add(type);
			}
		}
		
		List<String> areaPinyin2 = new ArrayList<String>();
		
		for (int i=0;i<areaPinyins.size();i++) {
			String oAreaPinyin = areaPinyins.get(i);
			String type = types.get(i);
			if(loadType==null){
				areaPinyin2.add(oAreaPinyin);
			}else{
				if(loadType.equals(type)){
					if(moreThanOne(oAreaPinyin , areaPinyins)){
						areaPinyin2.add(oAreaPinyin+areaCodes.get(i));
					}else{
						areaPinyin2.add(oAreaPinyin);
					}
				}
			}
		}
		return areaPinyin2;
	}

	private static void addToDao(Ali56DAOImpl dao, String url) {
		dao.create(new Ali56Entity("","",url));
	}

	private static List<String> loadCorpCodes(String fileName) {
		InputStream is = Ali56AreaUrlBuilder.class.getResourceAsStream(fileName);
		List<String> originalLines = TextLoader.loadText(is,"gbk");
		
		List<String> corpCodes = new ArrayList<String>();
		for (String line : originalLines) {
			String[] parts = line.split(",");

			String word = parts[0].replaceAll("\"", "").toLowerCase();
			corpCodes.add(word);
		}

		return corpCodes;
	}


	private static boolean moreThanOne(String oAreaPinyin, List<String> areaPinyins) {
		int count = 0;
		if(oAreaPinyin.startsWith("leshan")){
			count ++;
			count --;
		}
		for (String string : areaPinyins) {
			if(string.startsWith(oAreaPinyin)){
				count++;
			}
		}
		if(count>=2){
			return true;
		}
		return false;
	}

    private static String cutTail(String fullPinyin) {
        if (StringUtils.endsWith(fullPinyin, "shi")) {
            return fullPinyin.substring(0, fullPinyin.length() - 3);
        }
        if (StringUtils.endsWith(fullPinyin, "sheng")) {
            return fullPinyin.substring(0, fullPinyin.length() - 5);
        }
        if (StringUtils.endsWith(fullPinyin, "zizhiqu")) {
            return fullPinyin.substring(0, fullPinyin.length() - 7);
        }
        if (StringUtils.endsWith(fullPinyin, "diqu")) {
            return fullPinyin.substring(0, fullPinyin.length() - 4);
        }
        if (StringUtils.endsWith(fullPinyin, "qu")) {
            return fullPinyin.substring(0, fullPinyin.length() - 2);
        }
        if (StringUtils.endsWith(fullPinyin, "zizhixian")) {
            return fullPinyin.substring(0, fullPinyin.length() - 9);
        }
        if (StringUtils.endsWith(fullPinyin, "xian")) {
            return fullPinyin.substring(0, fullPinyin.length() - 4);
        }
        if (StringUtils.endsWith(fullPinyin, "zizhizhou")) {
            return fullPinyin.substring(0, fullPinyin.length() - 9);
        }
        return fullPinyin;
    }

}
