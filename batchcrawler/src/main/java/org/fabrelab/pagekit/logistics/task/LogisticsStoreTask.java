package org.fabrelab.pagekit.logistics.task;

import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.fabrelab.pagekit.logistics.model.Route;

public class LogisticsStoreTask extends StoreTask {

	private Route route;
	private TaskPool pool;
	
	public LogisticsStoreTask(TaskPool pool, Entity route) {
		this.route = (Route)route;
		this.pool = pool;
	}

	public void store()  {
		System.out.println(Thread.currentThread().getId() + ": Storing " + route.toPrintString() );
		pool.getEntityDAO().create(route);
	}

}
