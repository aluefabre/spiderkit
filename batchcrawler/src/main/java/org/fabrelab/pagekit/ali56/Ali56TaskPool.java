package org.fabrelab.pagekit.ali56;

import org.fabrelab.pagekit.ali56.model.Ali56Entity;
import org.fabrelab.pagekit.ali56.task.Ali56ProcessTask;
import org.fabrelab.pagekit.ali56.task.Ali56StoreTask;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;

public class Ali56TaskPool extends TaskPool {

	@Override
	public ProcessTask createProcessTask(Page page) {
		return new Ali56ProcessTask(this, page);
	}

	@Override
	public StoreTask createStoreTask(Entity route) {
		return new Ali56StoreTask(this, (Ali56Entity)route);
	}

}
