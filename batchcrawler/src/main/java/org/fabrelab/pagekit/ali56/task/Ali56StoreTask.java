package org.fabrelab.pagekit.ali56.task;

import org.fabrelab.pagekit.ali56.model.Ali56Entity;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.task.StoreTask;

public class Ali56StoreTask extends StoreTask {

	private Ali56Entity route;
	private TaskPool pool;
	
	public Ali56StoreTask(TaskPool pool, Ali56Entity route) {
		this.route = route;
		this.pool = pool;
	}

	public void store()  {
		System.out.println(Thread.currentThread().getId() + ": Storing " + route.toPrintString() );
		pool.getEntityDAO().create(route);
	}
	
}
