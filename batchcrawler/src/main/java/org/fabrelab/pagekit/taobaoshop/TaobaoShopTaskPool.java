package org.fabrelab.pagekit.taobaoshop;

import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.fabrelab.pagekit.taobaoshop.model.TaobaoShop;
import org.fabrelab.pagekit.taobaoshop.task.TaobaoShopProcessTask;
import org.fabrelab.pagekit.taobaoshop.task.TaobaoShopStoreTask;

public class TaobaoShopTaskPool extends TaskPool {

	@Override
	public ProcessTask createProcessTask(Page page) {
		return new TaobaoShopProcessTask(this, page);
	}

	@Override
	public StoreTask createStoreTask(Entity route) {
		return new TaobaoShopStoreTask(this, (TaobaoShop)route);
	}
}
