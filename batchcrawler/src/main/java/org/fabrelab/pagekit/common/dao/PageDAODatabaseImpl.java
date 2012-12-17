package org.fabrelab.pagekit.common.dao;

import java.util.HashMap;
import java.util.List;

import org.fabrelab.pagekit.common.model.Page;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class PageDAODatabaseImpl extends SqlMapClientDaoSupport implements PageDAO  {

	public List<Page> listPageByStatus(String status) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		return (List<Page>)getSqlMapClientTemplate().queryForList("listPageByStatus", map);
	}

	public List<Page> listPageByPriority(String status, Integer priority) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("priority", priority);
		return (List<Page>)getSqlMapClientTemplate().queryForList("listPageByPriority", map);
	}
	
	public Page getPageById(Long id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return (Page)getSqlMapClientTemplate().queryForObject("getPageById", map);
	}

	public void updatePage(Long id, String status) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", status);
		getSqlMapClientTemplate().update("updatePage", map );
	}

	public Long createPage(Page page) {
		Long id = (Long)getSqlMapClientTemplate().insert("createPage", page);
		page.setId(id);
		return id;
	}

	public void deletePage(Long id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		getSqlMapClientTemplate().delete("deletePage", map );
	}

	public List<Page> listPageByUrl(String url) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("url", url);
		return (List<Page>)getSqlMapClientTemplate().queryForList("listPageByUrl", map);
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
