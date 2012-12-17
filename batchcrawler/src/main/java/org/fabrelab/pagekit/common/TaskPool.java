package org.fabrelab.pagekit.common;

import java.util.List;

import org.fabrelab.pagekit.common.dao.EntityDAO;
import org.fabrelab.pagekit.common.dao.PageDAO;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;

public abstract class TaskPool {
	
	protected PageDAO pageDAO;
	private EntityDAO entityDAO;
	
	public Runnable take() throws InterruptedException {
		synchronized(TaskPool.class){
			ProcessTask pollFirst = tryFirstQueue();
			if (pollFirst != null) {
				pageDAO.updatePage(pollFirst.getPage().getId(), Page.STATUS_RUNNING);
				return pollFirst;
			}
			ProcessTask pollSecond = trySecondQueue();
			if (pollSecond != null) {
				pageDAO.updatePage(pollSecond.getPage().getId(), Page.STATUS_RUNNING);
				return pollSecond;
			}
		}
		return null;
	}
	
	private ProcessTask tryFirstQueue() {
		List<Page> shopPages = pageDAO.listPageByPriority(Page.STATUS_WAITING, 2);
		if(shopPages.size()>0){
			return createProcessTask(shopPages.get(0));
		}
		return null;
	}

	public abstract ProcessTask createProcessTask(Page page);

	private ProcessTask trySecondQueue() {
		List<Page> listPages = pageDAO.listPageByPriority(Page.STATUS_WAITING, 1);
		if(listPages.size()>0){
			return createProcessTask(listPages.get(0));
		}
		return null;
	}

	public void addTask(Page page) {
		List<Page> pages = pageDAO.listPageByUrl(page.getUrl());
		if(pages.size()==0){
			System.out.println(Thread.currentThread().getId() + ": Found Page " + page.getUrl());
			pageDAO.createPage(page);
		}
	}

	public PageDAO getPageDAO() {
		return pageDAO;
	}

	public void setPageDAO(PageDAO pageDAO) {
		this.pageDAO = pageDAO;
	}

	public EntityDAO getEntityDAO() {
		return entityDAO;
	}

	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	public abstract StoreTask createStoreTask(Entity route);

}
