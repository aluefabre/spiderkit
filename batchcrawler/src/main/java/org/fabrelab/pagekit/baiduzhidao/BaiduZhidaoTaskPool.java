package org.fabrelab.pagekit.baiduzhidao;

import org.fabrelab.pagekit.baiduzhidao.model.BaiduZhidaoQuestion;
import org.fabrelab.pagekit.baiduzhidao.task.BaiduZhidaoProcessTask;
import org.fabrelab.pagekit.baiduzhidao.task.BaiduZhidaoStoreTask;
import org.fabrelab.pagekit.common.TaskPool;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.common.model.Page;
import org.fabrelab.pagekit.common.task.ProcessTask;
import org.fabrelab.pagekit.common.task.StoreTask;

public class BaiduZhidaoTaskPool extends TaskPool {

	String word;
	QuestionProcessor processor;
	
	@Override
	public ProcessTask createProcessTask(Page page) {
		return new BaiduZhidaoProcessTask(this, page, word);
	}

	@Override
	public StoreTask createStoreTask(Entity route) {
		if(processor!=null){
			processor.process((BaiduZhidaoQuestion)route);
		}
		return new BaiduZhidaoStoreTask(this, (BaiduZhidaoQuestion)route);
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setProcessor(QuestionProcessor processor) {
		this.processor = processor;
	}
}
