package org.fabrelab.pagekit.baiduzhidao.task;

import org.fabrelab.pagekit.baiduzhidao.model.BaiduZhidaoQuestion;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.task.StoreTask;

public class BaiduZhidaoStoreTask extends StoreTask {

	private BaiduZhidaoQuestion question;
	private TaskPool pool;

	public BaiduZhidaoStoreTask(TaskPool pool, BaiduZhidaoQuestion question) {
		this.question = question;
		this.pool = pool;
	}

	public void store() {
		System.out.println(Thread.currentThread().getId() + ": Storing " + question.toPrintString());
		pool.getEntityDAO().create(question);
	}
}
