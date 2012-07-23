package com.yieldbroker.common.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadPool implements Executor{
	
	private final Executor executor;
	
	public ThreadPool() {
		this.executor = Executors.newCachedThreadPool();
	}
	
	public ThreadPool(int threads) {
		this.executor = Executors.newFixedThreadPool(threads);
	}

	@Override
	public void execute(Runnable command) {
		executor.execute(command);
	}

}
