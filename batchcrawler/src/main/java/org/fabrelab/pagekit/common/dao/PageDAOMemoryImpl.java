package org.fabrelab.pagekit.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.fabrelab.pagekit.common.model.Page;

public class PageDAOMemoryImpl implements PageDAO  {

	public static AtomicLong idseq = new AtomicLong(1L);
	
	public static HashMap<Long, Page> pages = new HashMap<Long, Page>();
		
	public List<Page> listPageByStatus(String status) {
		List<Page> result = new ArrayList<Page>();
		for(Page page : pages.values()){
			if(page.getStatus().equals(status)){
				result.add(page);
			}
		}
		return result;
	}

	public List<Page> listPageByPriority(String status, Integer priority) {
		List<Page> result = new ArrayList<Page>();
		for(Page page : pages.values()){
			if(page.getStatus().equals(status)){
				if(page.getPriority().equals(priority)){
					result.add(page);
				}
			}
		}
		return result;
	}
	
	public Page getPageById(Long id) {
		return pages.get(id);
	}

	public void updatePage(Long id, String status) {
		Page page = getPageById(id);
		page.setStatus(status);
	}

	public Long createPage(Page page) {
		page.setId(idseq.getAndIncrement());
		pages.put(page.getId(), page);
		return page.getId(); 
	}

	public void deletePage(Long id) {
		pages.remove(id);
	}

	public List<Page> listPageByUrl(String url) {
		List<Page> result = new ArrayList<Page>();
		for(Page page : pages.values()){
			if(page.getUrl().equals(url)){
				result.add(page);
			}
		}
		return result;
	}
	
	public void seed(List<String> seedUrls) {
		List<Page> pages = listPageByStatus(Page.STATUS_WAITING);
		if(pages.size()==0){
			for (String seedUrl : seedUrls) {
				Page page = new Page(seedUrl, Page.STATUS_WAITING, 1);
				createPage(page);
			}
		}
	}
}
