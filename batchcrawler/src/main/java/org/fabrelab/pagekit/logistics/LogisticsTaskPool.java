package org.fabrelab.pagekit.logistics;

import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.fabrelab.pagekit.logistics.task.LogisticsProcessTask;
import org.fabrelab.pagekit.logistics.task.LogisticsStoreTask;

public class LogisticsTaskPool extends TaskPool {

	@Override
	public ProcessTask createProcessTask(Page page) {
		return new LogisticsProcessTask(this, page);
	}

	@Override
	public StoreTask createStoreTask(Entity route) {
		return new LogisticsStoreTask(this, route);
	}
}
