package org.fabrelab.pagekit.common.task;

import java.util.List;
import java.util.Random;

import org.fabrelab.pagekit.URLHelper;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.dao.PageDAO;
import org.fabrelab.pagekit.common.model.Page;

public abstract class ProcessTask implements Runnable{

	protected Page page;

	protected PageDAO pageDAO;

	protected Random r = new Random();

	protected TaskPool pool;
	
	public ProcessTask(TaskPool pool, Page page) {
		this.page = page;
		this.pool = pool;
		this.pageDAO  = pool.getPageDAO();
	}

	public String fetch(String encoding) {
		URLHelper helper = new URLHelper(3,3);
		try {
			
			Thread.sleep(r.nextInt(6000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return helper.getUrl(page.getUrl(), encoding);
	}
	
	public void run() {
		try {
			List<StoreTask> stasks = this.process();
			for(StoreTask stask : stasks){
				stask.store();
			}
			pageDAO.updatePage(page.getId(), Page.STATUS_DONE);
		} catch (Exception e) {
			e.printStackTrace();
			pageDAO.updatePage(page.getId(), Page.STATUS_ERROR);
		}
	}

	public abstract List<StoreTask> process();

	public Page getPage() {
		return page;
	}

}
