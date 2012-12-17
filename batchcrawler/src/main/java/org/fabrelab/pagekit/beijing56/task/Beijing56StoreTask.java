package org.fabrelab.pagekit.beijing56.task;

import org.fabrelab.pagekit.beijing56.model.Beijing56Company;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.task.StoreTask;

public class Beijing56StoreTask extends StoreTask {

	private Beijing56Company route;
	private TaskPool pool;
	
	public Beijing56StoreTask(TaskPool pool, Beijing56Company route) {
		this.route = route;
		this.pool = pool;
	}

	public void store()  {
		System.out.println(Thread.currentThread().getId() + ": Storing " + route.toPrintString() );
		pool.getEntityDAO().create(route);
	}
	
}
