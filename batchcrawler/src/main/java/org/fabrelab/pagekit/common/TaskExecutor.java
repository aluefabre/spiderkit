package org.fabrelab.pagekit.common;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import org.fabrelab.pagekit.common.dao.EntityDAO;
import org.fabrelab.pagekit.common.dao.PageDAO;

public class TaskExecutor {

	private TaskPool taskPool;
	private PageDAO pageDAO;
	private EntityDAO entityDAO;
	private int threadCount = 3;

	AtomicInteger runningThreadCount = new AtomicInteger(0);
	AtomicInteger harvested = new AtomicInteger(0);

	public TaskExecutor() {

	}

	public void start() {
		List<Thread> threads = new Vector<Thread>();
		for (int i = 0; i < threadCount; i++) {
			Thread thread = buildTaskRunner(i);
			threads.add(thread);
		}
		for (Thread thread : threads) {
			thread.start();
		}
	}

	private Thread buildTaskRunner(int i) {
		Thread thread = new Thread("task-executor-" + i) {
			public void run() {
				try {
					while (true) {
						Runnable task = null;
						synchronized (TaskExecutor.class) {
							task = taskPool.take();
							boolean noMoreTask = (task == null && runningThreadCount.get() == 0);
							if(noMoreTask || entityDAO.enough()){
								if (harvested.getAndIncrement() == 0) {
									entityDAO.harvest();
								}
								break;
							}
							if (task != null) {
								runningThreadCount.incrementAndGet();
							}
						}
						if (task != null) {
							task.run();
							runningThreadCount.decrementAndGet();
						} else {
							Thread.sleep(3000);
						}
						if(pause){
							while(true){
								Thread.sleep(200);
								if(!pause){
									break;
								}
							}
						}
						if(stop){
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		return thread;
	}

	public TaskPool getTaskPool() {
		return taskPool;
	}

	public void setTaskPool(TaskPool taskPool) {
		this.taskPool = taskPool;
	}

	public PageDAO getPageDAO() {
		return pageDAO;
	}

	public void setPageDAO(PageDAO pageDAO) {
		this.pageDAO = pageDAO;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public EntityDAO getEntityDAO() {
		return entityDAO;
	}

	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}
	
	boolean pause = false;

	public boolean pause() {
		pause = !pause;
		return pause;
	}

	boolean stop = false;

	public void stop() {
		stop = true;
	}
}
