package org.fabrelab.pagekit.dianping;

import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;
import org.fabrelab.pagekit.dianping.task.DianpingProcessTask;
import org.fabrelab.pagekit.dianping.task.DianpingStoreTask;

public class DianpingTaskPool extends TaskPool {

	@Override
	public ProcessTask createProcessTask(Page page) {
		return new DianpingProcessTask(this, page);
	}

	@Override
	public StoreTask createStoreTask(Entity route) {
		return new DianpingStoreTask(this, route);
	}
}
