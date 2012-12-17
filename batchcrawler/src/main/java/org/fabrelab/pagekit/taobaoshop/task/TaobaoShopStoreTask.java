package org.fabrelab.pagekit.taobaoshop.task;

import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.fabrelab.pagekit.taobaoshop.model.TaobaoShop;

public class TaobaoShopStoreTask extends StoreTask {

	private TaobaoShop route;
	private TaskPool pool;
	
	public TaobaoShopStoreTask(TaskPool pool, TaobaoShop route) {
		this.route = route;
		this.pool = pool;
	}

	public void store()  {
		System.out.println(Thread.currentThread().getId() + ": Storing " + route.toPrintString() );
		pool.getEntityDAO().create(route);
	}
	
}
