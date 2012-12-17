package org.fabrelab.pagekit.dianping.task;

import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.fabrelab.pagekit.dianping.model.Shop;

public class DianpingStoreTask extends StoreTask {

	private TaskPool pool;
	private Shop shop;

	public DianpingStoreTask(TaskPool pool, Entity shop) {
		this.pool = pool;
		this.shop = (Shop)shop;
	}

	public void store()  {
		System.out.println(Thread.currentThread().getId() + ": Storing " + shop.getUrl() + " " + shop.getTitle());
		pool.getEntityDAO().create(shop);
	}

}
