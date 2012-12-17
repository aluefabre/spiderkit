package org.fabrelab.pagekit.common.dao;

import java.util.List;

import org.fabrelab.pagekit.common.model.Page;

public interface PageDAO  {

	public List<Page> listPageByStatus(String status);
	
	public List<Page> listPageByPriority(String status, Integer priority);
	
	public Page getPageById(Long id);

	public void updatePage(Long id, String status);

	public Long createPage(Page page);

	public void deletePage(Long id);

	public List<Page> listPageByUrl(String url);

	public void seed(List<String> seedUrl);
}
