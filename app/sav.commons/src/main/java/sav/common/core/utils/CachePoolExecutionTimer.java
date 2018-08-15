/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package sav.common.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LLT
 *
 */
public class CachePoolExecutionTimer extends ExecutionTimer {
	private static Logger log = LoggerFactory.getLogger(CachePoolExecutionTimer.class);
	private CustomizedThreadPoolExecutor executorService;
	
	protected CachePoolExecutionTimer(long defaultTimeout) {
		super(defaultTimeout);
	}

	@Override
	public boolean run(Runnable target, long timeout) {
		refreshExecutorService();
		executorService.execute(target);
		try {
			executorService.awaitTermination(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	private void refreshExecutorService() {
		if (executorService == null) {
			executorService = new CustomizedThreadPoolExecutor();
		}
	}
	
	private Map<Thread, Long> abandonedThreads = new HashMap<>();
	private class CustomizedThreadPoolExecutor extends ThreadPoolExecutor {
		private Map<Thread, Long> cachedRunningThreads = new HashMap<>();
		private Map<Runnable, Thread> runnableThreadMap = new HashMap<>();
		
		CustomizedThreadPoolExecutor() {
			super(0, Integer.MAX_VALUE,
                    60L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>());
		}

		@Override
		protected void beforeExecute(Thread t, Runnable r) {
			cachedRunningThreads.put(t, System.currentTimeMillis());
			runnableThreadMap.put(r, t);
			super.beforeExecute(t, r);
		}
		
		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			Thread correspondingThread = runnableThreadMap.remove(r);
			cachedRunningThreads.remove(correspondingThread);
			super.afterExecute(r, t);
		}
		
		@Override
		public List<Runnable> shutdownNow() {
			List<Runnable> runnables = super.shutdownNow();
			for (Thread runningThread : cachedRunningThreads.keySet()) {
				if (runningThread != null && runningThread.isAlive()) {
					abandonedThreads.put(runningThread, cachedRunningThreads.get(runningThread));
				}
			}
			cachedRunningThreads.clear();
			runnableThreadMap.clear();
			return runnables;
		}
	}
	
	public boolean cleanUpThreads() {
		if (executorService != null && !executorService.cachedRunningThreads.isEmpty()) {
			shutdown();
			return true;
		}
		return false;
	}

	@Override
	public void shutdown() {
		if (executorService != null) {
			executorService.shutdownNow();
			executorService = null;
			Timer timer = new Timer();
			final Map<Thread, Long> threadsToStop = new HashMap<>(abandonedThreads);
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					for (Thread thread : threadsToStop.keySet()) {
						if (thread != null && thread.isAlive()) {
							thread.stop();
						}
					}
				}
			}, 1000l);
			abandonedThreads.clear();
		}
	}
	
}
