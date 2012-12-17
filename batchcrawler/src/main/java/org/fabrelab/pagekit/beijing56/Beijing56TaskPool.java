package org.fabrelab.pagekit.beijing56;

import org.fabrelab.pagekit.beijing56.model.Beijing56Company;
import org.fabrelab.pagekit.beijing56.task.Beijing56ProcessTask;
import org.fabrelab.pagekit.beijing56.task.Beijing56StoreTask;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;

public class Beijing56TaskPool extends TaskPool {

	@Override
	public ProcessTask createProcessTask(Page page) {
		return new Beijing56ProcessTask(this, page);
	}

	@Override
	public StoreTask createStoreTask(Entity route) {
		return new Beijing56StoreTask(this, (Beijing56Company)route);
	}
}
